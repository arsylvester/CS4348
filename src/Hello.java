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
	    		 Pattern pattern = Pattern.compile("\\..*");  
	    		if(inScan.hasNext("^\\."))
	    		{
	    			System.out.println(inScan.next());
	    			//index = inScan.nextInt();
	    		}
	    		if(inScan.hasNext(pattern))
	    		{
	    			System.out.println(inScan.next());
	    			//index = inScan.nextInt();
	    		}
	    		inScan.nextLine();
	    	}
	    }
	    for(int z = 0; z < 30; z++)
	    	System.out.println(memory[z]);
   }

}