import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Memory 
{
	static int[] memory = new int[2000];
	static OutputStream os;
	
	public static void main(String[] args)
	{
		try
		{
			Scanner sc = new Scanner(System.in);
			 
			String fileName = null;
			if (sc.hasNext())
			   fileName = sc.nextLine(); 	
			
			// pass the path to the file as a parameter 
		    File file = 
		      new File(fileName); 
		    Scanner inScan = new Scanner(file); 
		  
		    int index = 0;
		    while (inScan.hasNext()) 
		    {
		    	if( inScan.hasNextInt())
		    	{

		    		memory[index++] = inScan.nextInt(); 
		    		System.out.println(memory[index-1]);
		    	}
		    	else
		    		inScan.next();
		    }
		    System.out.println("Command is " + memory[0]);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	public int read(int address)
	{
		return memory[address];
		//probably change to a pipe in.
	}
	
	public void write(int address, int data)
	{
		memory[address] = data;
	}

}
