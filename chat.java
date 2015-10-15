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
		if( f == true )
		{
			//lock.unlock();
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
	        clnt = new Thread(new client(st, lock, in, c.get()));//).start();
	        clnt.start();
		}else
		{
			serv = new Thread(new server(st, lock));//).start();
			clnt = new Thread(new client(st, lock, in));//).start();
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
