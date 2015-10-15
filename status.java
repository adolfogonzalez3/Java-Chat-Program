
public class status {

	public boolean st;
	
	public status()
	{
		st = false;
	}
	
	public synchronized void update(boolean b)
	{
		st = b;
	}
	
	public synchronized boolean get()
	{
		return st;
	}
	
}
