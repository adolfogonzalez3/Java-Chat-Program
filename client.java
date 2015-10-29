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
				int port = 9090;
				String s;
				// if the user wanted to wait for another user to connect then the client object will wait for the server object to get the
				// IP which is then passed to the client object through the contain object
				// if the user wanted to connect to another user then the client object will ask the user for an IP
				if( flag == true )
				{
					System.out.print("What do you want to connect to?: ");
					s = in.nextLine();
				}else
				{
					s = ip;
				}
				client = new Socket(s, port);
				System.out.println("Just connected to " + client.getRemoteSocketAddress());
				DataOutputStream out = new DataOutputStream(client.getOutputStream()); // message to server
				String input;
				boolean flag = false;
				String localIP = client.getLocalAddress().toString().substring(1);
				while(!st.get() && !flag)
				{
					if( System.in.available() != 0 )
					{
						input = in.nextLine();
						if( !(input.length() == 0) )
						{
							if( input.charAt(0) == '/' )
							{
								// the quit or exit command must be preceded by a forward slash
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
				st.update(true);
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
