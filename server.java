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
	}
	
	public server(status stat, Lock l, contain c, chat ch )
	{
		flag = true;
		cont = c;
		chatter = ch;
		st = stat;
		lock = l;
	}
	
	public void run() {
			 ServerSocket ss = null;
			 // if the user wants to wait for another user to connect then the server object will wait for the other user to connect
			 // and then get the address of the other user which is passed to the chat which is then passed to the server
			 // if the user wants to connect to another user then the server waits for the other user to respond
			try {
				ss = new ServerSocket(9090);
				ss.setSoTimeout(100000);
				
				try
				{
					Socket server = null;
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
					DataInputStream in = new DataInputStream(server.getInputStream()); // message from client
					String input;
					boolean flag = false;
					DataOutputStream out = new DataOutputStream(server.getOutputStream()); // message to client
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
					st.update(true);
					server.close();
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
