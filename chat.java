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
		 
		 Thread serv, clnt;
		 
		if( f == true )
		{
			contain c = new contain();
			serv = new Thread(new server(c));
			serv.start();
			synchronized(serv)
			{
				try{
	                serv.wait();
	            }catch(InterruptedException e){
	                e.printStackTrace();
	            }
			}
			
	        clnt = new Thread(new client(in, c.get()));//).start();
	        clnt.start();
		}else
		{
			serv = new Thread(new server());//).start();
			clnt = new Thread(new client(in));//).start();
			serv.start();
			clnt.start();
		}
		
		synchronized(clnt)
		{
			try{
                clnt.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
		}

		in.close();
	}
	
	 public static void main(String args[]) {
		(new Thread(new chat())).start();
		 
    }
}
