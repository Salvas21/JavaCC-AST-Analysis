import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;


public class NetworkClient extends ANetworkModel
{
	int _port;
	String _adresse;
	Socket _socket;
	Network _network;
	NetworkMsg _lastMsg;
	
	public NetworkClient(String adresse, int port, Network network)
	{
		_port = port;
		_adresse = adresse;
		_socket = null;
		_keepRunning = false;
		_network = network;
		_lastMsg = null;
	}

	public void close()
	{
		try
		{
			if (_socket != null && _socket.isClosed() == false)
			{
				_socket.close();
				_socket = null;
			}
   			_keepRunning = false;
		}
   		catch(IOException e)
		{
		}
	}
	
	public final boolean connect()
	{
		try 
		{
			_socket = new Socket(_adresse, _port);
			return true;
		}
   		catch(IOException e)
		{
   			return false;
		}
	}

	public void run()
	{
		byte[] stand = new byte[0];
		
		_keepRunning = true;
		while (_socket != null && _keepRunning == true)
		{
			try
			{
				InputStream iStream = _socket.getInputStream();
				
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
	   			_keepRunning = false;
	   			_network.clientDisconnect("Host disconnect.");
			}
		}
	}

	private void usingReceiveMsg(NetworkMsg msg)
	{
		if (_network.getGameState() == Client.gameState.Room)
			usingReceiveMsgRoom(msg);
		else if (_network.getGameState() == Client.gameState.Game)
			usingReceiveMsgGame(msg);
	}

	private void respondeReceive(boolean value)
	{
		switch (_lastMsg.getHeader())
		{
		case ChangeTeamPlayer :
			if (value == true)
			{
				int team = NetworkMsg.bytesToInt(_lastMsg.getData(), 0);

				_network.getClient().setTeam(team);
			}
			break;
		case ChangeNamePlayer :
			if (value == true)
			{
				String name = "";
				
				for (int i = 0; i < _lastMsg.getData().length; i++)
					name += (char)_lastMsg.getData()[i];
				_network.getClient().getYourInfo().SetName(name);
				_network.getClient().setCurrentName(name);
			}
			break;
		case RunGame :
			if (value == true)
				_network.getClient().launchGame();
			break;
		default :
			break;
		}
	}
	
	private void runGameReceive()
	{
		_network.getClient().launchGame();
	}
	
	private void usingReceiveMsgRoom(NetworkMsg msg)
	{
		switch (msg.getHeader())
		{
		case OK :
			respondeReceive(true);
			break;
		case KO :
			respondeReceive(false);
			break;
		case KickPlayerRoom :
			kickPlayerRoom();
			break;
		case ListPlayerRoom :
			listPlayerRoom(msg);
			break;
		case ChangeNamePlayer :
			changeNamePlayer(msg);
			break;
		case ChangeTeamPlayer :
			changeTeamPlayer(msg);
			break;
		case DeletedRoom :
			deletedRoom();
			break;
		case RunGame :
			runGameReceive();
			break;
		default :
			break;
		}
	}
	
	private void usingReceiveMsgGame(NetworkMsg msg)
	{
		switch (msg.getHeader())
		{
		case Shoot :
			shootReceive(msg);
			break;
		case MvtTarget :
			mvtTargetReceive(msg);
			break;
		case KillTarget :
			killTargetReceive(msg);
			break;
		case OutTarget :
			outTargetReceive(msg);
			break;
		case ChangeScore :
			changeScoreReceive(msg);
			break;
		case PlayerTurn :
			playerTurnReceive(msg);
			break;
		case EndGame :
			endGameReceive(msg);
			break;
		case PosForShoot :
			posForShootReceive(msg);
			break;
		case KickPlayerRoom :
			kickPlayerRoom();
			break;
		case DeletedRoom :
			kickPlayerRoom();
			break;
		case Pull :
			pullReceive();
			break;
		case Mark :
			markReceive();
			break;
		case Double :
			doubleReceive();
			break;
		case Fire :
			fireReceive();
			break;
		default :
			break;
		}
	}

	private void mvtTargetReceive(NetworkMsg msg)
	{
		if (msg.getData().length < 32)
			return ;
		int id = NetworkMsg.bytesToInt(msg.getData(), 0);
		int xlength = NetworkMsg.bytesToInt(msg.getData(), 8);
		int ylength = NetworkMsg.bytesToInt(msg.getData(), 16 + xlength);
		int zlength = NetworkMsg.bytesToInt(msg.getData(), 24 + xlength + ylength);
		if (xlength <= 0 || ylength <= 0 || zlength <= 0)
			return ;
		String xStr = "";
		String yStr = "";
		String zStr = "";

		for (int i = 0; i < xlength; i++)
			xStr += (char)msg.getData()[16 + i];
		for (int i = 0; i < ylength; i++)
			yStr += (char)msg.getData()[24 + xlength + i];
		for (int i = 0; i < zlength; i++)
			zStr += (char)msg.getData()[32 + xlength + ylength + i];
		_network.getClient().MvtTargetReceive(id, Double.valueOf(xStr), Double.valueOf(yStr), Double.valueOf(zStr));
	}
	
	private void killTargetReceive(NetworkMsg msg)
	{
		_network.getClient().KillTargetReceive(NetworkMsg.bytesToInt(msg.getData(), 0));
	}
	
	private void outTargetReceive(NetworkMsg msg)
	{
		_network.getClient().OutTargetReceive(NetworkMsg.bytesToInt(msg.getData(), 0));
	}
	
	private void changeScoreReceive(NetworkMsg msg)
	{
		if (msg.getData().length < 8)
			return ;
		String name = "";
		int score = NetworkMsg.bytesToInt(msg.getData(), 0);
		
		for (int i = 8; i < msg.getData().length; i++)
			name += (char)msg.getData()[i];
		_network.getClient().ChangeScoreReceive(name, score);
	}
	
	private void playerTurnReceive(NetworkMsg msg)
	{
		String name = "";
		for (int i = 0; i < msg.getData().length; i++)
			name += (char)msg.getData()[i];
		_network.getClient().PlayerTurnReceive(name);
	}
	
	private void endGameReceive(NetworkMsg msg)
	{		
		if (msg.getData().length < 24)
			return ;
		_network.getClient().EndGameReceive(NetworkMsg.bytesToInt(msg.getData(), 0), 
											NetworkMsg.bytesToInt(msg.getData(), 8), 
											NetworkMsg.bytesToInt(msg.getData(), 16));
	}
	
	private void posForShootReceive(NetworkMsg msg)
	{
		_network.getClient().PosForShootReceive(NetworkMsg.bytesToInt(msg.getData(), 0));
	}
	
	private void pullReceive()
	{
		_network.getClient().PullReceive();
	}
	
	private void markReceive()
	{
		_network.getClient().MarkReceive();
	}

	private void doubleReceive()
	{
		_network.getClient().DoubleReceive();
	}
	
	private void fireReceive()
	{
		_network.getClient().fireReceive();
	}
	
	private void shootReceive(NetworkMsg msg)
	{
		if (msg.getData().length < 16)
			return ;
		int x = NetworkMsg.bytesToInt(msg.getData(), 0);
		int y = NetworkMsg.bytesToInt(msg.getData(), 8);
		
		_network.getClient().playerShootReceive(x, y);
	}
	
	private void kickPlayerRoom()
	{
		_network.clientDisconnect("Kick from the room.");
	}

	private void changeNamePlayer(NetworkMsg msg)
	{
		String name = "";

		for (int i = 0; i < msg.getData().length; i++)
			name += (char)msg.getData()[i];
		_network.getClient().getYourInfo().SetName(name);
		_network.getClient().setCurrentName(name);
		_network.getClient().setFlagChangeName(true);
	}
	
	private void changeTeamPlayer(NetworkMsg msg)
	{
		if (msg.getData().length < 8)
			return ;
		int team = NetworkMsg.bytesToInt(msg.getData(), 0);
		_network._client.setTeam(team);
	}
	
	private void listPlayerRoom(NetworkMsg msg)
	{
		if (msg.getData().length < 32)
			return ;
		int lenght1, lenght2, lenght3, lenght4;
		String namePlayer1Team1 = "", namePlayer2Team1 = "", namePlayer1Team2 = "", namePlayer2Team2 = "";

		lenght1 = NetworkMsg.bytesToInt(msg.getData(), 0);
		lenght2 = NetworkMsg.bytesToInt(msg.getData(), 8);
		lenght3 = NetworkMsg.bytesToInt(msg.getData(), 16);
		lenght4 = NetworkMsg.bytesToInt(msg.getData(), 24);
		
		for (int i = 32; i < (32 + lenght1); i++)
			namePlayer1Team1 += (char)msg.getData()[i];
		for (int i = (32 + lenght1); i < (32 + lenght1 + lenght2); i++)
			namePlayer2Team1 += (char)msg.getData()[i];
		for (int i = (32 + lenght1 + lenght2); i < (32 + lenght1 + lenght2 + lenght3); i++)
			namePlayer1Team2 += (char)msg.getData()[i];
		for (int i = (32 + lenght1 + lenght2 + lenght3); i < (32 + lenght1 + lenght2 + lenght3 + lenght4); i++)
			namePlayer2Team2 += (char)msg.getData()[i];

	// /* Debug */	System.out.println("List Player recu  : Team 1 : " + namePlayer1Team1 + " - " + namePlayer2Team1 + "\nTeam 2 :  " + namePlayer1Team2 + " - " + namePlayer2Team2);
		_network.getClient().updateRoom(namePlayer1Team1, namePlayer2Team1, namePlayer1Team2, namePlayer2Team2);
	}

	
	public void sendShoot(int x, int y)
	{
		byte[] data = new byte[16];

		System.arraycopy(NetworkMsg.intToBytes(x), 0, data, 0, 8);
		System.arraycopy(NetworkMsg.intToBytes(y), 0, data, 8, 8);
		sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Shoot, data));
	}
	
	private void deletedRoom()
	{
		_network.clientDisconnect("Room deleted.");
	}

	public void sendFire()
	{
		sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Fire, new byte[0]));
	}
	
	public void runGame()
	{
		_lastMsg = new NetworkMsg(NetworkMsg.msgHeader.RunGame, new byte[0]);
		sendMsg(_lastMsg);
	}

	public void changeName(String name)
	{
		_lastMsg = new NetworkMsg(NetworkMsg.msgHeader.ChangeNamePlayer, name.getBytes());
		sendMsg(_lastMsg);
	}
	
	public void changeTeam(int team)
	{
		_lastMsg = new NetworkMsg(NetworkMsg.msgHeader.ChangeTeamPlayer, NetworkMsg.intToBytes(team));
		sendMsg(_lastMsg);
	}
	
	public void kickClient()
	{
		sendMsg(new NetworkMsg(NetworkMsg.msgHeader.KickPlayerRoom, new byte[0]));
		_network.clientDisconnect("Quit Room");
	}
	
	public void sendMsg(NetworkMsg msg)
	{
		byte[] send = msg.arrayByteToSend();

		try
		{
			OutputStream oStream = _socket.getOutputStream();
			
			oStream.write(send);
		}
   		catch(IOException e)
		{
   			_keepRunning = false;
   			_network.clientDisconnect("Host didn't respond.");
		}
	}
}
