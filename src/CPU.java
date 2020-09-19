import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CPU 
{
	public static void main(String[] args) 
	{
		try 
		{
			int PC = 0, SP, IR = 0, AC, X, Y;
			
			//Start another java process to act as our memory.
			Runtime rt = Runtime.getRuntime();
			Process memory = rt.exec("java Memory.java");
			
			//CPU To Memory pipe
			OutputStream cpuToMem = memory.getOutputStream();
			// Memory To CPU pipe
			InputStream memToCPU = memory.getInputStream();
			
			//Print writer to write to Memory.
			PrintWriter pw = new PrintWriter(cpuToMem);
			
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
				//Read next instruction
				pw.println(PC);
				pw.flush();
				value = "";
				
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
				System.out.println(IR);
				PC++;
			}
			//clean up and exit
			pw.flush();
			pw.close();
			cpuToMem.close();

			memory.waitFor();

			int exitVal = memory.exitValue();

			System.out.println("Process exited: " + exitVal);

		} 
		catch (Throwable t) 
		{
			t.printStackTrace();
		}
	}

}
