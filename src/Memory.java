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
		    	}
		    	else
		    	{
		    		inScan.nextLine();
		    	}
		    }
		    
		    //System.out.println("This is the memory");
		    
		    while(sc.hasNext())
		    {
		    	int x = sc.nextInt();
		    	if(x == 1)
		    	{
			    	//System.out.println("Memory to read");
		    		System.out.println(read(sc.nextInt()));
		    	}
		    	else
		    	{
			    	//System.out.println("50");
		    		write(sc.nextInt(), sc.nextInt());
		    	}
		    }
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}
	
	public static int read(int address)
	{
		return memory[address];
		//probably change to a pipe in.
	}
	
	public static void write(int address, int data)
	{
		memory[address] = data;
	}

}
