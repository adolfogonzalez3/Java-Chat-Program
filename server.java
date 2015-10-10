import java.io.DataInputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class server implements Runnable{

	public boolean flag;
	public contain cont;
	private chat chatter;
	
	public server()
	{
		flag = false;
	}
	
	public server(contain c, chat ch )
	{
		flag = true;
		cont = c;
		chatter = ch;
	}
	
	public void run() {
			 ServerSocket ss = null;
			try {
				ss = new ServerSocket(9090);
				ss.setSoTimeout(100000);
				
				while(true)
				{
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
						
						DataInputStream in = new DataInputStream(server.getInputStream()); // message from client
						while(true)
						{
							System.out.println(in.readUTF());
						}
						//DataOutputStream out = new DataOutputStream(server.getOutputStream()); // message to client
						//out.writeUTF("Thank you for connecting to Adolfo's server " + server.getLocalSocketAddress() +". Goodbye!");
						//server.close();
						//System.out.println("Done!");
					}catch(SocketTimeoutException s)
					{ 
						System.out.println("Socket timed out!"); 
						break; 
					}catch(IOException e)
					{ 
						e.printStackTrace(); 
						break; 
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally
			{
				try {
					ss.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
	    }
	
}
