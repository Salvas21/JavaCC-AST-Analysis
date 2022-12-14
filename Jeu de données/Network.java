
public class Network
{
	ANetworkModel _networkModel;
	Client _client;

	public Network(Client client)
	{
		_networkModel = null;
		_client = client;
	}
	
	public boolean createServer(int port)
	{
		_networkModel =  new NetworkServer(port, this);
		if (_networkModel.connect() == true)
		{
			if (_client.getYourInfo().GetName() == "")
				_client.getYourInfo().SetName("Player 1");
			_networkModel.start();
			_networkModel.addClient(null, _client.getYourInfo().GetName(), 1);
			return true;
		}
		_client.setError("Can't create Server.");
		return false;
	}

	public void quitRoom()
	{
		if (_networkModel != null)
			_networkModel.kickClient();
	}

	public boolean connectToServer(String adresse, int port)
	{
		_networkModel =  new NetworkClient(adresse, port, this);
		if (_networkModel.connect() == true)
		{
			_networkModel.start();
			return true;
		}
		_client.setError("Can't connect to Server.");
		return false;
	}

	public void clearConnect()
	{
		if (_networkModel != null)
		{
			_networkModel._keepRunning = false;
			_networkModel.close();
			_networkModel = null;
		}
	}
	
	public void clientDisconnect(String explain)
	{
		if (_client.getGameState() == Client.gameState.Room)
		{
		//	if (_networkModel != null)
		//		_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.DeletedRoom, new byte[0]));
			clearConnect();
			_client.quitRoom(explain);
		}
		else
		{
		//	if (_networkModel != null)
		//		_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.DeletedRoom, new byte[0]));
			clearConnect();
			_client.quitGame(explain);
		}
	    _networkModel = null;
	}

	public void serverKill(String explain)
	{
		if (_client.getGameState() == Client.gameState.Room)
		{
			if (_networkModel != null)
				_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.DeletedRoom, new byte[0]));
			clearConnect();
			_client.quitRoom(explain);
		}
		else
		{
			if (_networkModel != null)
				_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.DeletedRoom, new byte[0]));
			clearConnect();
			_client.quitGame(explain);
		}
	    _networkModel = null;
	}

	public void sendFire()
	{
		if (_networkModel != null)
			_networkModel.sendFire();
	}

	public void Fire()
	{
		_client.Fire();
	}
	
	public void Fire(boolean isDouble, boolean isPull)
	{
		if (_networkModel != null)
			_networkModel.Fire(isDouble, isPull);
	}

	public void runGame()
	{
		if (_networkModel != null)
			_networkModel.runGame();
	}

	public void changeName(String name)
	{
		if (_networkModel != null)
			_networkModel.changeName(name);
	}
	
	public void changeTeam(int team)
	{
		if (_networkModel != null)
			_networkModel.changeTeam(team);
	}
	
	public Client.gameState getGameState()
	{
		return _client.getGameState();
	}
	
	public Client getClient()
	{
		return _client;
	}
	
	public void changeStation(int station)
	{
		if (_networkModel != null)
			_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.PosForShoot, NetworkMsg.intToBytes(station)));
	}
	
	public void changePlayerTurn(String name)
	{
		if (_networkModel != null)
			_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.PlayerTurn, name.getBytes()));
	}

	public void sendEndGame(int score1, int score2, int teamWin)
	{
		byte[] data = new byte[24];

		System.arraycopy(NetworkMsg.intToBytes(score1), 0, data, 0, 8);
		System.arraycopy(NetworkMsg.intToBytes(score2), 0, data, 8, 8);
		System.arraycopy(NetworkMsg.intToBytes(teamWin), 0, data, 16, 8);
		if (_networkModel != null)
			_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.EndGame, data));
	}

	public void changeScoreTeam(int score, String player)
	{
		byte[] data = new byte[8 + player.getBytes().length];

		System.arraycopy(NetworkMsg.intToBytes(score), 0, data, 0, 8);
		System.arraycopy( player.getBytes(), 0, data, 8, player.getBytes().length);
		if (_networkModel != null)
			_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.ChangeScore, data));
	}

	public void killTarget(int id)
	{
		if (_networkModel != null)
			_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.KillTarget, NetworkMsg.intToBytes(id)));
	}

	public void outTarget(int id)
	{
		if (_networkModel != null)
			_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.OutTarget, NetworkMsg.intToBytes(id)));
	}
	
	public void mvtTarget(int id, double x, double y, double z)
	{
		int pos = 0;
		byte[] xtab = Double.toString(x).getBytes();
		byte[] ytab = Double.toString(y).getBytes();
		byte[] ztab = Double.toString(z).getBytes();
		byte[] data = new byte[32 + xtab.length + ytab.length + ztab.length];

		System.arraycopy(NetworkMsg.intToBytes(id), 0, data, pos, 8);
		pos += 8;
		System.arraycopy(NetworkMsg.intToBytes(xtab.length), 0, data, pos, 8);
		pos += 8;
		System.arraycopy(xtab, 0, data, pos, xtab.length);
		pos += xtab.length;
		System.arraycopy(NetworkMsg.intToBytes(ytab.length), 0, data, pos, 8);
		pos += 8;
		System.arraycopy(ytab, 0, data, pos, ytab.length);
		pos += ytab.length;
		System.arraycopy(NetworkMsg.intToBytes(ztab.length), 0, data, pos, 8);
		pos += 8;
		System.arraycopy(ztab, 0, data, pos, ztab.length);	
		if (_networkModel != null)
			_networkModel.sendMsg(new NetworkMsg(NetworkMsg.msgHeader.MvtTarget, data));
	}
	
	public void sendShoot(int x, int y)
	{
		if (_networkModel != null)
			_networkModel.sendShoot(x, y);
	}

	public boolean isHost()
	{
		if (_networkModel == null)
			return false;
		return _networkModel.isHost();
	}
}
