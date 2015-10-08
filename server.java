import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class server implements Runnable{

	public boolean flag;
	public contain cont;
	
	public server()
	{
		flag = false;
	}
	
	public server(contain c )
	{
		flag = true;
		cont = c;
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
						Socket server;
						//System.out.println("Waiting for client on port " +ss.getLocalPort() + "...");
						System.out.println("Server IP: " + ss.getLocalSocketAddress() );				
						//System.out.println("Just connected to " + server.getRemoteSocketAddress());
						if( flag )
						{
							synchronized(this)
							{
								server = ss.accept();
								cont.update(server.getRemoteSocketAddress().toString());
								notify();
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
