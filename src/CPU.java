import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CPU 
{
	static int PC = 0, SP = 999, IR = 0, AC = 0, X = 0, Y = 0;
	static PrintWriter pw;
	static InputStream memToCPU;
	
	public static void main(String[] args) 
	{
		try 
		{
			//int PC = 0, SP = 0, IR = 0, AC = 0, X = 0, Y = 0;
			
			//Start another java process to act as our memory.
			Runtime rt = Runtime.getRuntime();
			Process memory = rt.exec("java Memory.java");
			
			//CPU To Memory pipe
			OutputStream cpuToMem = memory.getOutputStream();
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
			while (PC < 30 && IR != 50)
			{
				readMemory(PC++);
				
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
						readMemory(PC++);
						readMemory(IR + SP);
						AC = IR;
						break;
					//LoadSpX
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
						writeMemory(SP--, PC);
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
						writeMemory(SP--, AC);
						break;
					//Pop
					case 28:
						readMemory(SP++);
						AC = IR;
						break;
					//Int 
					case 29:
						//System call
						break;
					//IRet
					case 30:
						//return from system call
						break;
					//Exit
					case 50:
						break;
					default:
						break;
				}
			}
			//clean up and exit
			pw.flush();
			pw.close();
			cpuToMem.close();
			memToCPU.close();

			memory.waitFor();

			int exitVal = memory.exitValue();

			System.out.println("Process exited: " + exitVal);

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
			//Read next instruction
			pw.println(1);
			pw.println(address);
			pw.flush();
			String value = "";
			
			//Get bytes from memory and convert to int
			IR = memToCPU.read();
			while(IR != 13)
			{
				value += (char)IR;
				IR = memToCPU.read();
			}
			
			//Skip over the line return
			memToCPU.skip(1);
			
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
