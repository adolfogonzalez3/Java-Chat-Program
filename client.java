import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class client implements Runnable{

	public boolean flag;
	public String ip;
	
	public client()
	{
		flag = true;
	}
	
	public client(String s)
	{
		flag = false;
		ip = s;
	}
	
	public void run()
	{
		Scanner in = null;
		Socket client = null;
		try
		{
			in = new Scanner(System.in);
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
			client = new Socket(s, port);
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			DataOutputStream out = new DataOutputStream(client.getOutputStream()); // message to server
			while(true)
			{
				out.writeUTF(client.getLocalAddress() + ": " + in.nextLine());
			}
			//out.writeUTF(	"Hello from Adolfo’s client " + client.getLocalSocketAddress());
			//DataInputStream in = new DataInputStream(client.getInputStream()); // message from server
			//System.out.println("Server says " +	in.readUTF());
			//client.close();
		}catch(IOException e)
		{
			System.out.println("There was an error!");
		}finally
		{
			try {
				if( client != null )
					client.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			in.close();
		}
	}
}
