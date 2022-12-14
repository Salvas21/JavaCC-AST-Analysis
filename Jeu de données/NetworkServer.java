import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class NetworkServer extends ANetworkModel
{
	int _port;
	ServerSocket _server;
	ArrayList<ClientConnected> _list;
	Network _network;
	
	public NetworkServer(int port, Network network)
	{
		_port = port;
		_server = null;
		_keepRunning = false;
		_list = new ArrayList<ClientConnected>();
		_network = network;
	}
	
	public void close()
	{
		try
		{
			if (_server != null && _server.isClosed() == false)
			{
				_server.close();
				_server = null;
			}
			for (int i = 0; i < _list.size(); i++)
			{
				_list.get(i).close();
			}
			_list.clear();
   			_keepRunning = false;
		}
   		catch(IOException e)
		{
		}
	}
	
	private String findName(int i)
	{
		String name = "Player " + Integer.toString(i);
		
		for (int j = 0; j < _list.size(); j++)
		{
			if (_list.get(j).getNameClient().compareTo(name) == 0)
				return findName(i + 1);
		}
		return name;
	}
	
	public boolean nameAlreadyExist(String name)
	{
		for (int j = 0; j < _list.size(); j++)
		{
			if (_list.get(j).getNameClient().compareTo(name) == 0)
				return true;
		}
		return false;
	}

	private int findTeam()
	{
		int nbTeam1 = 0;
		
		for (int j = 0; j < _list.size(); j++)
		{
			if (_list.get(j).getTeam() == 1)
				nbTeam1++;
		}
		if (nbTeam1 == 2)
			return 2;
		return 1;
	}
	
	public boolean freePlaceInTeam(int team)
	{
		int nbTeam1 = 0;
		int nbTeam2 = 0;
		
		for (int j = 0; j < _list.size(); j++)
		{
			if (_list.get(j).getTeam() == 1)
				nbTeam1++;
			if (_list.get(j).getTeam() == 2)
				nbTeam2++;
		}
		if (team == 1 && nbTeam1 < 2)
			return true;
		if (team == 2 && nbTeam2 < 2)
			return true;
		return false;
	}
	
	public boolean canRun()
	{
		if (freePlaceInTeam(1) == false && freePlaceInTeam(2) == false)
			return true;
		return false;
	}
	
	public final boolean connect()
	{
	   try
	   {
			_server = new ServerSocket(_port);
			return true;
		}
   		catch(IOException e)
		{
		//	System.out.printf("Server Failed : %s\n", e.toString());
   			return false;
		}
	}

	public void updateRoomInfo()
	{
		String namePlayer1Team1 = "";
		String namePlayer2Team1 = "";
		String namePlayer1Team2 = "";
		String namePlayer2Team2 = "";
		int flag1 = 0, flag2 = 0;

		for (int j = 0; j < _list.size(); j++)
		{
			if (_list.get(j).getTeam() == 1)
			{
				flag1++;
				if (flag1 == 1)
					namePlayer1Team1 = _list.get(j).getNameClient();
				else
					namePlayer2Team1 = _list.get(j).getNameClient();
			}
			if (_list.get(j).getTeam() == 2)
			{
				flag2++;
				if (flag2 == 1)
					namePlayer1Team2 = _list.get(j).getNameClient();
				else
					namePlayer2Team2 = _list.get(j).getNameClient();
			}
		}
		_network.getClient().updateRoom(namePlayer1Team1, namePlayer2Team1, namePlayer1Team2, namePlayer2Team2);
		byte[] toSend = new byte[32 + namePlayer1Team1.length() + namePlayer2Team1.length() + namePlayer1Team2.length() + namePlayer2Team2.length()];
		System.arraycopy(NetworkMsg.intToBytes(namePlayer1Team1.length()), 0, toSend, 0, 8);
		System.arraycopy(NetworkMsg.intToBytes(namePlayer2Team1.length()), 0, toSend, 8, 8);
		System.arraycopy(NetworkMsg.intToBytes(namePlayer1Team2.length()), 0, toSend, 16, 8);
		System.arraycopy(NetworkMsg.intToBytes(namePlayer2Team2.length()), 0, toSend, 24, 8);
		flag1 = 32;
		System.arraycopy(namePlayer1Team1.getBytes(), 0, toSend, flag1, namePlayer1Team1.getBytes().length);
		flag1 += namePlayer1Team1.getBytes().length;
		System.arraycopy(namePlayer2Team1.getBytes(), 0, toSend, flag1, namePlayer2Team1.getBytes().length);
		flag1 += namePlayer2Team1.getBytes().length;
		System.arraycopy(namePlayer1Team2.getBytes(), 0, toSend, flag1, namePlayer1Team2.getBytes().length);
		flag1 += namePlayer1Team2.getBytes().length;
		System.arraycopy(namePlayer2Team2.getBytes(), 0, toSend, flag1, namePlayer2Team2.getBytes().length);
		sendMsg(new NetworkMsg(NetworkMsg.msgHeader.ListPlayerRoom, toSend));
	}
	
	private void sendHisOwnInfo(ClientConnected player)
	{
		player.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.ChangeNamePlayer, player.getNameClient().getBytes()));
		player.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.ChangeTeamPlayer, NetworkMsg.intToBytes(player.getTeam())));
	}
	
	public boolean isHost(ClientConnected player)
	{
		if (_list.size() == 0 || _list.get(0) != player)
			return false;
		return true;
	}

	public void deleteRoom()
	{
		sendMsg(new NetworkMsg(NetworkMsg.msgHeader.DeletedRoom, new byte[0]));
		_network.serverKill("Room deleted");
	}
	
	public void addClient(Socket client, String name, int team)
	{
		_list.add(new ClientConnected(client, name, team, this));
		_list.get(_list.size() - 1).start();
		sendHisOwnInfo(_list.get(_list.size() - 1));
		updateRoomInfo();
	}

	public void runGame()
	{
		if (canRun() == true)
		{
			_network.getClient().launchGameServer();
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.RunGame, new byte[0]));
		}
	}

	public void changeName(String name)
	{
		if (name.length() >  0 && nameAlreadyExist(name) == false)
		{
			_network.getClient().getYourInfo().SetName(name);
			_list.get(0).setNameClient(name);
			_network.getClient().setCurrentName(name);
			_network.getClient().setFlagChangeName(true);
			updateRoomInfo();
		}
	}
	
	public void changeTeam(int team)
	{
		if (freePlaceInTeam(team) == true)
		{
			_list.get(0).setTeam(team);
			_network.getClient().setTeam(team);
			updateRoomInfo();
		}
	}
	
	public void run()
	{
		_keepRunning = true;
		while (_server != null && _keepRunning == true)
		{
			try
			{
				Socket client = _server.accept();
				if (_list.size() <= 3)
				{
					addClient(client, findName(1), findTeam());
				}
			}
	   		catch(IOException e)
			{
	   			_keepRunning = false;
				//System.out.printf("Server Failed : %s\n", e.toString());
			}
		}
	}

	public void sendMsg(NetworkMsg msg)
	{
		for (int i = 0; i < _list.size(); i++)
			_list.get(i).sendMsg(msg);
	}

	public void sendMsg(NetworkMsg msg, ClientConnected player)
	{
		player.sendMsg(msg);
	}

	public void sendMsg(NetworkMsg msg, String name)
	{
		for (int i = 0; i < _list.size(); i++)
			if (_list.get(i).getNameClient().compareTo(name) == 0)
				_list.get(i).sendMsg(msg);
	}
	
	public void kickPlayer(String name)
	{
		for (int i = 0; i < _list.size(); i++)
			if (_list.get(i).getNameClient().compareTo(name) == 0)
			{
				_list.get(i).setKeepRunning(false);
				disconnectClient(_list.get(i));
			}
	}
	
	public void disconnectClient(ClientConnected client)
	{
		_list.remove(client);
		if (_network.getGameState() == Client.gameState.Room)
			updateRoomInfo();
		else if (_network.getGameState() == Client.gameState.Game)
			kickClient();//_network.serverKill("");
	}

	public void Fire(boolean isDouble, boolean isPull)
	{
		if (isDouble == true)
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Double, new byte[0]));
		else if (isPull == true)
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Pull, new byte[0]));
		else
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Mark, new byte[0]));
	}

	public void sendFire()
	{
		int team = (_network.getClient().getGame().getPlayerTurn() / 2) + 1;
		int player = (_network.getClient().getGame().getPlayerTurn() % 2) + 1;
		
		if (team == _list.get(0).getTeam() && player == 1)
			_network.Fire();
	}
	
	public void Fire(ClientConnected playerSender)
	{
		int team = (_network.getClient().getGame().getPlayerTurn() / 2);
		int player = (_network.getClient().getGame().getPlayerTurn() % 2);
		
		if (_network.getClient().getGame().getRoom().GetTeam(Room.TeamNumber.values()[team]).GetPlayerList()[player].GetName().compareTo(playerSender.getNameClient()) == 0)
		{
			_network.Fire();
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Fire, new byte[0]));
		}
	}

	public void sendShoot(int x, int y)
	{
		int team = (_network.getClient().getGame().getPlayerTurn() / 2);
		int player = (_network.getClient().getGame().getPlayerTurn() % 2);

		if (_network.getClient().getGame().getRoom().GetTeam(Room.TeamNumber.values()[team]).GetPlayerList()[player].GetName().compareTo(_list.get(0).getNameClient()) == 0 &&
				_network.getClient().getGame().getShootDone() == false)
		{
			byte[] data = new byte[16];

			System.arraycopy(NetworkMsg.intToBytes(x), 0, data, 0, 8);
			System.arraycopy(NetworkMsg.intToBytes(y), 0, data, 8, 8);
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Shoot, data));
			_network.getClient().getGame().Shoot(x, y);
		}
	}

	public void Shoot(int x, int y, ClientConnected playerSender)
	{
		int team = (_network.getClient().getGame().getPlayerTurn() / 2);
		int player = (_network.getClient().getGame().getPlayerTurn() % 2);

		if (_network.getClient().getGame().getRoom().GetTeam(Room.TeamNumber.values()[team]).GetPlayerList()[player].GetName().compareTo(playerSender.getNameClient()) == 0 &&
				_network.getClient().getGame().getShootDone() == false)
		{
			byte[] data = new byte[16];

			System.arraycopy(NetworkMsg.intToBytes(x), 0, data, 0, 8);
			System.arraycopy(NetworkMsg.intToBytes(y), 0, data, 8, 8);
			sendMsg(new NetworkMsg(NetworkMsg.msgHeader.Shoot, data));
			_network.getClient().getGame().Shoot(x, y);
		}
	}
	
	public void kickClient()
	{
		sendMsg(new NetworkMsg(NetworkMsg.msgHeader.DeletedRoom, new byte[0]));
		_network.serverKill("Quit Room");
	}
	
	public Network getNetwork()
	{
		return _network;
	}

	public boolean isHost()
	{
		return true;
	}
}
