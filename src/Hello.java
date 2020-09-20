import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.*;

public class Hello
{
   public static void main(String args[]) throws FileNotFoundException
   {
	   String fileName = "TestData.txt";
	   int[] memory = new int[2000];
		
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
	    		// Pattern pattern = Pattern.compile("\\..*");  
	    		if(inScan.hasNext("\\..*"))
	    		{
	    			try 
	    			{
		    			int newAddress = Integer.parseInt(inScan.next().substring(1));
		    			//System.out.println(newAddress);
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
	    for(int z = 0; z < 30; z++)
	    	System.out.println(memory[z]);
   }

}