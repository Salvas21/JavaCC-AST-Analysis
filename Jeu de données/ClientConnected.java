import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;


public class ClientConnected extends Thread
{
	Socket _client;
	String _nameClient;
	boolean _keepRunning;
	int _team;
	NetworkServer _server;

	
	public void close()
	{
		try
		{
			if (_client != null && _client.isClosed() == false)
			{
				_client.close();
				_client = null;
			}
			_keepRunning = false;
		}
   		catch(IOException e)
		{
		}
	}
	
	public ClientConnected(Socket client, int team, NetworkServer server)
	{
		_client = client;
		_nameClient = "";
		_keepRunning = false;
		_server = server;
		_team = team;
	}

	public ClientConnected(Socket client, String name, int team, NetworkServer server)
	{
		_client = client;
		_nameClient = name;
		_keepRunning = false;
		_server = server;
		_team = team;
	}
	
	public void run()
	{
		byte[] stand = new byte[0];
		
		_keepRunning = true;
		while (_client != null && _keepRunning == true)
		{
			try
			{
				InputStream iStream = _client.getInputStream();
				
				byte[] b = new byte[1000];
				int bitsRecus = iStream.read(b);
				if (bitsRecus > 0)
				{
					byte[] newStand = new byte[bitsRecus + stand.length];
					System.arraycopy(stand, 0, newStand, 0, stand.length);
					System.arraycopy(b, 0, newStand, stand.length, bitsRecus);
					stand = newStand;
					Pair<List<NetworkMsg>, byte[]> dataReceive = NetworkMsg.fromBytes(stand, stand.length);
					stand = dataReceive.second;
					for (int i = 0; i < dataReceive.first.size(); i++)
						usingReceiveMsg(dataReceive.first.get(i));
				}
			}
	   		catch(IOException e)
			{
				_server.disconnectClient(this);
				_keepRunning = false;
			}
		}
	}

	private void usingReceiveMsg(NetworkMsg msg)
	{
		// /* Debug */	 System.out.println("Msg recu : " + msg.toString());
		if (_server.getNetwork().getGameState() == Client.gameState.Room)
			usingReceiveMsgRoom(msg);
		else if (_server.getNetwork().getGameState() == Client.gameState.Game)
			usingReceiveMsgGame(msg);
	}
	
	private void usingReceiveMsgRoom(NetworkMsg msg)
	{
		switch (msg.getHeader())
		{
		case ChangeTeamPlayer :
			changedTeam(msg);
			break;
		case ChangeNamePlayer :
			changedName(msg);
			break;
		case RunGame :
			runGame();
			break;
		case DeletedRoom :
			deleteRoom(msg);
			break;
		case KickPlayerRoom :
			kickPlayer();
			break;
		default :
			break;
		}
	}
	
	private void usingReceiveMsgGame(NetworkMsg msg)
	{
		switch (msg.getHeader())
		{
		case Fire :
			_server.Fire(this);
			break;
		case Shoot :
			shoot(msg);
			break;
		default :
			break;
		}
	}
	
	private void shoot(NetworkMsg msg)
	{
		int x = NetworkMsg.bytesToInt(msg.getData(), 0);
		int y = NetworkMsg.bytesToInt(msg.getData(), 8);
		
		_server.Shoot(x, y, this);
	}
	
	private void runGame()
	{
		if (_server.isHost(this) == false || _server.canRun() == true)
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.KO, new byte[0]));
		else
		{
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.OK, new byte[0]));
			_server._network.getClient().launchGame();
		}
	}
	
	private void deleteRoom(NetworkMsg msg)
	{
		if (_server.isHost(this)  == false)
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.KO, new byte[0]));
		else
		{
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.OK, new byte[0]));
			_server.deleteRoom();
		}
	}
	
	private void kickPlayer()
	{
		_server.kickPlayer(_nameClient);
		_server.updateRoomInfo();
	}
	
	private void changedName(NetworkMsg msg)
	{
		byte[] data = msg.getData();
		String name = "";
		
		for (int i = 0; i < data.length; i++)
			name += (char)data[i];
		if (name.length() <= 0 || _server.nameAlreadyExist(name) == true)
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.KO, new byte[0]));
		else
		{
			_nameClient = name;
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.OK, new byte[0]));
			_server.updateRoomInfo();
		}
	}
	
	private void changedTeam(NetworkMsg msg)
	{
		int requestTeam = NetworkMsg.bytesToInt(msg.getData(), 0);

		if (_server.freePlaceInTeam(requestTeam) == true)
		{
			_team = requestTeam;
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.OK, new byte[0]));
			_server.updateRoomInfo();
		}
		else
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.KO, new byte[0]));
	}
	
	public void sendMsg(NetworkMsg msg)
	{
		if (_client == null)
			return ;
		byte[] send = msg.arrayByteToSend();

		try
		{
			OutputStream oStream = _client.getOutputStream();

			oStream.write(send);
		}
   		catch(IOException e)
		{
			_server.disconnectClient(this);
			_keepRunning = false;
		}
	}

	public String getNameClient()
	{
		return _nameClient;	
	}
	
	public void setNameClient(String name)
	{
		_nameClient = name;
	}

	public int getTeam()
	{
		return _team;	
	}
	
	public void setTeam(int team)
	{
		_team = team;
	}
	
	public boolean getKeepRunning()
	{
		return _keepRunning;	
	}
	
	public void setKeepRunning(boolean state)
	{
		_keepRunning = state;
	}
}
