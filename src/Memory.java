//Memory.java created by Andrew Sylvester for CS 4348
//This code is created as a separate process by CPU.java. It reads an input file and stores it into an array. Then when the cpu requests it will either read or write to that array.
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
			
			//Pass the path to the file as a parameter 
		    File file = new File(fileName); 
		    Scanner inScan = new Scanner(file); 
		  
		    //Loop through input file gathering instructions
		    int index = 0;
		    while (inScan.hasNext()) 
		    {
		    	if( inScan.hasNextInt())
		    	{
		    		memory[index++] = inScan.nextInt(); 
		    	}
		    	else
		    	{
		    		//Check if memory location to store system calls
		    		if(inScan.hasNext("\\..*"))
		    		{
		    			try 
		    			{
		    				//Get the string after the . and convert to int.
			    			int newAddress = Integer.parseInt(inScan.next().substring(1));
			    			index = newAddress;
		    			}
		    			catch(Throwable e)
		    			{
		    				//System.out.println("Not an int");
		    			}
		    		}
		    		inScan.nextLine();
		    	}
		    }
		    
		    //Loop until CPU is done processing instructions
		    while(sc.hasNext())
		    {
		    	int x = sc.nextInt();
		    	//Read memory
		    	if(x == 1)
		    	{
		    		System.out.println(read(sc.nextInt()));
		    	}
		    	//Write memory
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
	}
	
	public static void write(int address, int data)
	{
		memory[address] = data;
	}

}
