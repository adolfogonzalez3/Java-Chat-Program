import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class chat implements Runnable{
	
	final Lock lock = new ReentrantLock();
	
	public void run()
	{
		Scanner in = new Scanner(System.in);
		 boolean flag = false;
		 boolean f = false;
		 // First we ask the user if they want to wait for another client to connect to them
		 // or they want to connect to another waiting client
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
		 status st = new status();
		 // If the user wants to wait, f == true, we make an object contain which allows both processes to talk to one another
		 // next the server is made and the chat object waits until the server has the IP of the connecting client its setup before continuing
		 // the client then starts with the IP address from the connecting client
		 // if the user doesn't want to wait then the user provides an IP address which is used to connect the user's client with another client
		if( f == true )
		{
			contain c = new contain();
			serv = new Thread(new server(st, lock,c,this));
			serv.start();			
			synchronized(this)
			{
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			c.update(trimToIP(c.get()));
	        clnt = new Thread(new client(st, lock, in, c.get()));
	        clnt.start();
		}else
		{
			serv = new Thread(new server(st, lock));
			clnt = new Thread(new client(st, lock, in));
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
	// trims the remotesocketaddress string 
	public String trimToIP(String str)
	{
		String temp = "";
		boolean flag = false;
		for( int i = 1; i < str.length() && !flag; i++ )
		{
			if(str.charAt(i) != ':' )
			{
				temp += str.charAt(i);
			}else
			{
				flag = true;
			}
		}
		return temp;
	}
	
	 public static void main(String args[]) {
		(new Thread(new chat())).start();
		 
    }
}
