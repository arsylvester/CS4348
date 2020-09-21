import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CPU 
{
	//Initializes most variables and 'registers'. I know having most of these be static isn't the most proper java/OOP practice, but nor is processes and pipes in java and this project isn't supposed to demonstrate anything OOP. 
	//So I'm doing this to keep things clean and have functions access them.
	static int PC = 0, SP = 0, IR = 0, AC = 0, X = 0, Y = 0, timer;
	static Boolean kernel = false;
	static final int TIMER_INTER_LOC = 1000, INT_INSTR_LOC = 1500, USER_STACK = 1000, SYSTEM_STACK = 2000;
	static PrintWriter pw;
	static InputStream memToCPU;
	static OutputStream cpuToMem;
	static Process memory;
	
	public static void main(String[] args) 
	{
		try 
		{
			//Initialize timer and timer max
			timer = Integer.parseInt(args[1]);
			int timerMax = timer;
			
			//Initialize stack to user stack. 
			//NOTE on SP. Any time something is pushed to stack will --SP, any time popping using SP++. So SP points to the top element, not the one after it.
			SP = USER_STACK;
			
			//Start another java process to act as our memory.
			Runtime rt = Runtime.getRuntime();
			memory = rt.exec("java Memory");
			
			//CPU To Memory pipe
			cpuToMem = memory.getOutputStream();
			// Memory To CPU pipe
			memToCPU = memory.getInputStream();
			
			//Print writer to write to Memory.
			pw = new PrintWriter(cpuToMem);
			
			//Send the name of the input text file for memory to read
			String fileName = args[0];
			pw.printf(fileName + "\n");
			//pw.println("0");
			//pw.println("1");
			//pw.println("2");
			pw.flush();



			String value;
			//Get instruction from memory. This will be our core loop.
			while (IR != 50)
			{
				if(timer <= 0 && !kernel)
				{
					IR = 29;
				}
				else
				{
					readMemory(PC++);
				}
				switch(IR)
				{
					//Load value
					case 1:
						readMemory(PC++);
						AC = IR;
						break;
					//Load value at address
					case 2:
						readMemory(PC++);
						readMemory(IR);
						AC = IR;
						break;
					//LoadInd addr   
					case 3:
						readMemory(PC++);
						readMemory(IR);
						readMemory(IR);
						AC = IR;
						break;
					//LoadIdxX addr
					case 4:
						readMemory(PC++);
						readMemory(IR + X);
						AC = IR;
						break;
					//LoadIdxY addr
					case 5:
						readMemory(PC++);
						readMemory(IR + Y);
						AC = IR;
						break;
					//LoadSpX
					case 6:
						readMemory(SP + X);
						AC = IR;
						break;
					//Store addr
					case 7:
						readMemory(PC++);
						writeMemory(IR, AC);
						break;
					//Get
					case 8:
						AC = (int) ((Math.random() * 100) + 1);
						break;
					//Put Port
					case 9:
						readMemory(PC++);
						
						//Write AC to screen as an int
						if(IR == 1)
						{
							System.out.print(AC);
						}
						//Write AC to screen as an char
						else
						{
							System.out.print((char)AC);
						}
						break;
					//AddX
					case 10:
						AC += X;
						break;
					//AD Y;
					case 11:
						AC += Y;
						break;
					//SubX
					case 12:
						AC -= X;
						break;
					//SubY
					case 13:
						AC -= Y;
						break;
					// CopyToX
					case 14:
						X = AC;
						break;
					//CopyFromX
					case 15:
						AC = X;
						break;
					//CopyToY
					case 16:
						Y = AC;
						break;
					//CopyFromY
					case 17:
						AC = Y;
						break;
					//CopyToSp
					case 18:
						SP = AC;
						break;
					//CopyFromSp   
					case 19:
						AC = SP;
						break;
					//Jump addr
					case 20:
						readMemory(PC++);
						PC = IR;
						break;
					//JumpIfEqual addr
					case 21:
						readMemory(PC++);
						if(AC == 0)
							PC = IR;
						break;
					//JumpIfNotEqual addr
					case 22:
						readMemory(PC++);
						if(AC != 0)
							PC = IR;
						break;
					//Call addr
					case 23:
						writeMemory(--SP, PC);
						readMemory(PC++);
						PC = IR;
						break;
					//Ret 
					case 24:
						readMemory(SP++);
						PC = IR;
						break;
					//IncX 
					case 25:
						X++;
						break;
					//DecX 
					case 26:
						X--;
						break;
					//Push
					case 27:
						writeMemory(--SP, AC);
						break;
					//Pop
					case 28:
						readMemory(SP++);
						AC = IR;
						break;
					//Int 
					case 29:
						//Check we are not already handling an interrupt
						if(kernel)
						{
							break;
						}
						kernel = true;
						
						//Push registers to system stack. Also pushing AC, X, and Y in case user program was using them, we don't want to override when we return.
						int tempSP = SP;
						SP = SYSTEM_STACK;
						writeMemory(--SP, tempSP);
						writeMemory(--SP, PC);
						writeMemory(--SP, AC);
						writeMemory(--SP, X);
						writeMemory(--SP, Y);
						
						//Check if a timer interrupt or system call
						if(timer <= 0)
						{
							PC = TIMER_INTER_LOC;
						}
						else
						{
							PC = INT_INSTR_LOC;
						}
						break;
					//IRet
					case 30:
						//Check we are in an interrupt
						if(!kernel)
						{
							break;
						}
						readMemory(SP++);
						Y = IR;
						readMemory(SP++);
						X = IR;
						readMemory(SP++);
						AC = IR;
						readMemory(SP++);
						PC = IR;
						readMemory(SP++);
						SP = IR;
						
						kernel = false;
						
						// If return from timer interrupt than reset timer
						if(timer <= 0)
						{
							timer = timerMax + 1; //We do + 1 because the timer will be decreased at the end of the IRet instruction since we are no longer in kernel mode, but we want to start counting down on the next user instruction.
						}
						break;
					//Exit
					case 50:
						break;
					default:
						break;
					
				}
				//Only decrease the timer if handling user instructions, not an interrupt
				if(!kernel)
				{
					timer--;
				}
			}
			//clean up and exit
			pw.flush();
			pw.close();
			cpuToMem.close();
			memToCPU.close();

			memory.waitFor();

			//int exitVal = memory.exitValue();

			//System.out.println("Process exited: " + exitVal);

		} 
		catch (Throwable t) 
		{
			t.printStackTrace();
		}
	}
	
	public static void readMemory(int address)
	{
		try
		{
			//Check if trying to access system memory when not able to
			if(address >= 1000 && !kernel)
			{
				System.out.println("Memory violation: accessing system address " + address + " in user mode");
				//clean up and exit
				pw.flush();
				pw.close();
				cpuToMem.close();
				memToCPU.close();

				memory.waitFor();
				System.exit(0);
			}
			
			//Read next instruction
			pw.println(1);
			pw.println(address);
			pw.flush();
			String value = "";
			
			//Get bytes from memory and convert to int
			IR = memToCPU.read();
			do
			{
				value += (char)IR;
				IR = memToCPU.read();
			}
			while(IR != 13 && IR != 10);
			
			//For Some reason windows gets the bytes 13 then 10 at the end of the instructions, but Linux only finds the 10 (new line). So this check is skips the 10 if using windows.
			if(IR == 13)
			{
				memToCPU.skip(1);
			}
			
			//Set IR to instruction value.
			IR = Integer.parseInt(value);
			//System.out.println(IR);
			//PC++;
		}
		catch (Throwable t) 
		{
			t.printStackTrace();
		}
	}
	
	public static void writeMemory(int address, int value)
	{
		try
		{
			//Check if trying to access system memory when not able to
			if(address >= 1000 && !kernel)
			{
				System.out.println("Memory violation: accessing system address " + address + " in user mode");
				//clean up and exit
				pw.flush();
				pw.close();
				cpuToMem.close();
				memToCPU.close();

				memory.waitFor();
				System.exit(0);
			}
			
			//Write value at address
			pw.println(0);
			pw.println(address);
			pw.println(value);
			pw.flush();
		}
		catch (Throwable t) 
		{
			t.printStackTrace();
		}
	}
}
