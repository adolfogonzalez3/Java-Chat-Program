import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class client implements Runnable{

	Scanner in;
	public boolean flag;
	public String ip;
	public Lock lock;
	private status st;
	
	public client(status stat, Lock l, Scanner passed)
	{
		flag = true;
		in = passed;
		lock = l;
		st = stat;
	}
	
	public client(status stat, Lock l, Scanner passed, String s)
	{
		flag = false;
		ip = s;
		in = passed;
		lock = l;
		st = stat;
	}
	
	public void run()
	{
		synchronized(this)
		{
			Socket client = null;
			try
			{
				//in = new Scanner(System.in);
				int port = 9090;
				String s;
				if( flag == true )
				{
					System.out.print("What do you want to connect to?: ");
					s = in.nextLine();
				}else
				{
					s = ip;
				}
				/*synchronized(lock)
				{
					while(lock.tryLock())
					{
						
					}
				}*/
				client = new Socket(s, port);
				System.out.println("Just connected to " + client.getRemoteSocketAddress());
				DataOutputStream out = new DataOutputStream(client.getOutputStream()); // message to server
				String input;
				boolean flag = false;
				/*
				try {
					this.wait(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//System.out.println("Before client lock.");
				/*synchronized(lock)
				{
					while(!lock.tryLock())
					{
						
					}
					lock.notify();
				}*/
				//System.out.println("After client lock.");
				//System.out.println(client.getLocalAddress());
				String localIP = client.getLocalAddress().toString().substring(1);
				//System.out.println(localIP);
				/*
				if( lock.tryLock() )
				{
					System.out.println("Have client lock.");
				}
				*/
				while(!st.get() && !flag)
				{
					if( System.in.available() != 0 )
					{
						input = in.nextLine();
						if( !(input.length() == 0) )
						{
							if( input.charAt(0) == '/' )
							{
								if( input.compareTo("/quit") == 0 || input.compareTo("/exit") == 0 )
								{
									flag = true;
									out.writeUTF( input );
								}else
								{
									System.out.println("System error: Not a command.(quit or exit)");
								}
							}else
							{
								out.writeUTF(localIP + ": " + input );
							}
						}
					}					
				}
				//System.out.println("After client while loop.");
				st.update(true);
				//out.writeUTF(	"Hello from Adolfo’s client " + client.getLocalSocketAddress());
				//DataInputStream in = new DataInputStream(client.getInputStream()); // message from server
				//System.out.println("Server says " +	in.readUTF());
				/*
				if(lock.tryLock())
				{
					lock.unlock();
					System.out.println("Client unlocked lock.");
				}
				*/
				client.close();
			}catch(IOException e)
			{
				System.out.println("There was an error!");
			}finally
			{
				try {
					if( !client.isClosed() )
					{
						client.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.notify();
		}		
	}
}
