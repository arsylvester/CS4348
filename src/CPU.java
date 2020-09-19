import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CPU 
{
	int PC, SP, IR, AC, X, Y;
	
	public static void main(String[] args) 
	{
		try 
		{
			//Start another java process to act as our memory.
			Runtime rt = Runtime.getRuntime();
			Process memory = rt.exec("java Memory.java");
			
			//CPU To Memory pipe
			OutputStream cpuToMem = memory.getOutputStream();
			
			//Print writer to write to Memory.
			PrintWriter pw = new PrintWriter(cpuToMem);
			
			//Send the name of the input text file for memory to read
			String fileName = args[0];
			pw.printf(fileName + "\n");
			pw.flush();
			
			// Memory To CPU pipe
			InputStream MemToCPU = memory.getInputStream();
			
			//Print the instruction TESTING REMOVE
			int instruction;
			while ((instruction = MemToCPU.read()) != -1)
				System.out.print((char) instruction);

			//clean up and exit
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
