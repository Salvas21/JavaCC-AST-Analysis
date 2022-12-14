import java.net.Socket;


public abstract class ANetworkModel extends Thread
{
	protected boolean _keepRunning;

	public void addClient(Socket client, String name, int team)
	{
	}

	public void kickClient()
	{
	}

	public void runGame()
	{
	}

	public void changeName(String name)
	{
	}
	
	public void changeTeam(int team)
	{
	}
	
	public boolean connect()
	{
		return false;
	}

	public void Fire(boolean isDouble, boolean isPull)
	{
	}

	public void sendFire()
	{	
	}
	
	public boolean getKeepRunning()
	{
		return _keepRunning;	
	}
	
	public void setKeepRunning(boolean state)
	{
		_keepRunning = state;
	}

	public void sendMsg(NetworkMsg msg)
	{
	}

	public void sendShoot(int x, int y)
	{
	}

	public void close()
	{
	}
	
	public boolean isHost()
	{
		return false;
	}
}
