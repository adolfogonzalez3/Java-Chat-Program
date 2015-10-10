
public class contain {
	public String str;
	public contain()
	{
		str = "";
	}
	
	public synchronized void update(String s )
	{
		str = s;
	}
	
	public synchronized String get()
	{
		return str;
	}
}
