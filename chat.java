import java.util.Scanner;

public class chat implements Runnable{
	
	public void run()
	{
		Scanner in = new Scanner(System.in);
		 boolean flag = false;
		 boolean f = false;
		 while(!flag)
		 {
			  System.out.print("Do you want to wait for someone else to connect to you?: ");
			  String ans = in.nextLine();
			  ans = ans.toLowerCase();
			  if( ans.compareTo("yes") == 0 || ans.compareTo("y") == 0 )
			  {
				  flag = true;
				  f = true;
			  }else if( ans.compareTo("no") == 0 || ans.compareTo("n") == 0 )
			  {
				  flag = true;
				  f = false;
			  }
		 }
		in.close();
		 
		if( f == true )
		{
			contain c = new contain();
			Thread b = new Thread(new server(c));
			b.start();
			synchronized(b)
			{
				try{
	                System.out.println("Waiting for b to complete...");
	                b.wait();
	            }catch(InterruptedException e){
	                e.printStackTrace();
	            }
			}
			
	        (new Thread(new client(c.get()))).start();
		}else
		{
			(new Thread(new server())).start();
			(new Thread(new client())).start();
		}
	}
	
	 public static void main(String args[]) {
		(new Thread(new chat())).start();
		 
    }
}
