import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.locks.Lock;

public class server implements Runnable{

	public boolean flag;
	public contain cont;
	private chat chatter;
	private Lock lock;
	private status st;
	
	public server(status stat, Lock l)
	{
		flag = false;
		lock = l;
		st = stat;
		/*try {
			lock.tryLock(5,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.lock();*/
	}
	
	public server(status stat, Lock l, contain c, chat ch )
	{
		flag = true;
		cont = c;
		chatter = ch;
		st = stat;
		lock = l;
		/*
		try {
			lock.tryLock(5,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lock.lock();*/
	}
	
	public void run() {
			 ServerSocket ss = null;
			try {
				ss = new ServerSocket(9090);
				ss.setSoTimeout(100000);
				
				try
				{
					Socket server = null;
					//System.out.println("Waiting for client on port " +ss.getLocalPort() + "...");
					//System.out.println("Server IP: " + ss.get );				
					//System.out.println("Just connected to " + server.getRemoteSocketAddress());
					if( flag == true )
					{
						synchronized(chatter)
						{
							server = ss.accept();
							cont.update(server.getRemoteSocketAddress().toString());
							chatter.notify();
						}
					}else
					{
						server = ss.accept();
					}
					//System.out.println("Before Server lock.");
					/*synchronized(lock)
					{
						try {
							lock.unlock();
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}*/
					//System.out.println("After Server lock.");
					DataInputStream in = new DataInputStream(server.getInputStream()); // message from client
					String input;
					boolean flag = false;
					DataOutputStream out = new DataOutputStream(server.getOutputStream()); // message to client
					//out.writeUTF("Thank you for connecting to Adolfo's server " + server.getLocalSocketAddress() +". Goodbye!");
					while(!st.get() && !flag)
					{	
						if( in.available() != 0 )
						{
							input = in.readUTF();
							if( !(input.length() == 0 ) )
							{
								if( input.charAt(0) == '/' )
								{		
									if( input.compareTo("/exit") == 0 || input.compareTo("/quit") == 0 )
									{
										flag = true;
									}else
									{
										out.writeUTF("System error: Not a command.(quit or exit)");
									}
								}else
								{
									System.out.println(input);
								}	
							}					
						}
												
					}
					//System.out.println("After server while loop.");
					st.update(true);
					/*
					if( lock.tryLock() )
					{
						lock.unlock();
						System.out.println("Server unlocked lock.");
					}
					*/
					server.close();
					//System.out.println("Done!");
				}catch(SocketTimeoutException s)
				{ 
					System.out.println("Socket timed out!"); 
				}catch(IOException e)
				{ 
					e.printStackTrace(); 
				}
				ss.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally
			{
				if( !ss.isClosed() )
				{
					try {
						ss.close();
						System.out.println("Server was closed.");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
	    }
	
}
