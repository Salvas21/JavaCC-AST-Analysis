
public class Client
{
	public enum gameState
	{
		Menu,
		Room,
		Game,
	}
	
	Network _network;
	Room _room;
	gameState _gameState;
	Player _yourInfo;
	int _yourTeam;
	Game _game;
	String _currentName;
	String _error;
	boolean _flagChangeName;
	boolean _flagQuitRoom;
	boolean _flagQuitGame;
	boolean _flagLaunchGame;
	
	Client()
	{
		_network = new Network(this);
		init();
	}
	
	public void init()
	{
		_room = new Room();
		_gameState = gameState.Room;
		_yourInfo = new Player(Team.PlayerType.PLAYER_ONE, Room.TeamNumber.TEAM_ONE);
		_yourTeam = 1;
		_currentName = "Player 1";
		_error = "";
		_flagChangeName = true;
		_flagQuitRoom = false;
		_flagQuitGame = false;
		_flagLaunchGame = false;
	}
	
	public boolean createRoom(int port)
	{
		init();
		_network.clearConnect();
		if (_network.createServer(port) == true)
		{
			_gameState = gameState.Room;
			return true;
		}
		return false;
	}

	public boolean joinRoom(String addr, int port)
	{
		init();
		_network.clearConnect();
		if (_network.connectToServer(addr, port) == true)
		{
			_gameState = gameState.Room;
			return true;
		}
		return false;
	}
	
	public void clearNetwork()
	{
		if (_network != null)
			_network.clearConnect();
	}

	public void quitRoom(String reason)
	{
		clearNetwork();
		_error = reason;
		_flagQuitRoom = true;
		_gameState = gameState.Menu;
	}
	
	public void quitGame(String reason)
	{
		clearNetwork();
	//	_error = reason;
		_flagQuitGame = true;
		_gameState = gameState.Menu;
	}
	
	public void runGame() // Utilisateur : lancer la partie
	{
		if (_network != null)
			_network.runGame();
	}

	public void quitRoom() // Utilisateur : quitter le jeu
	{
		if (_network != null)
			_network.quitRoom();
	}

	public void changeName(String name) // Utilisateur : Changer de nom
	{
		if (_network != null)
			_network.changeName(name);
	}

	public void changeTeam() // Utilisateur : Changer l'équipe
	{
		if (_network != null)
			_network.changeTeam((_yourTeam == 1 ? 2 : 1));
	}
	
	public void changeTeam(int team) // Utilisateur : Changer l'équipe
	{
		if (_network != null)
			_network.changeTeam(team);
	}

	public void clickFire() // Utilisateur : Bouton Fire
	{
		_network.sendFire();
	}

	public void ClickShoot(int x, int y) // Utilisateur : Tire du joueur
	{
		_network.sendShoot(x, y);
	}
	
	public void launchGame() // lancement de la partie sans la boucle de jeu (sert uniquement de conteneur pour les données
	{
		_gameState = Client.gameState.Game;
		_game = new Game(_room, this);
		_flagLaunchGame = true;
	}

	public void launchGameServer() // lancement de la partie avec la boucle de jeu
	{
		_gameState = Client.gameState.Game;
		_game = new Game(_room, this);
		_game.start();
		_flagLaunchGame = true;
	}
	
	public void endGame(int scoreTeam1, int scoreTeam2, int idTeamWin)
	{
		_gameState = Client.gameState.Menu;
		if (_game.isAlive() == true)
			_game.endGame();
		_game = null;
	}

	public void Fire()
	{
		_game.Fire();
	}

	public void Fire(boolean isDouble, boolean isPull)
	{
		_network.Fire(isDouble, isPull);
	}
	
	public void sendEndGame(int score1, int score2, int teamWin)
	{
		_network.sendEndGame(score1, score2, teamWin);
		clearNetwork();
	}

	public void changeScoreTeam(int score, String player)
	{
		_network.changeScoreTeam(score, player);
	}

	public void killTarget(int id)
	{
		_network.killTarget(id);
	}

	public void outTarget(int id)
	{
		_network.outTarget(id);
	}
	
	public void mvtTarget(int id, double x, double y, double z)
	{
		_network.mvtTarget(id, x, y, z);
	}
	
	public void updateRoom(String team1Player1, String team1Player2, String team2Player1, String team2Player2)
	{
		_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).SetName(team1Player1);
		_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).SetName(team1Player2);
		_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).SetName(team2Player1);
		_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).SetName(team2Player2);
	}
	
	public void changeStation(int station)
	{
		_network.changeStation(station);
	}
	
	public void changePlayerTurn(String name)
	{
		_network.changePlayerTurn(name);
	}
	
	public Network getNetwork()
	{
		return _network;
	}

	public Room getRoom()
	{
		return _room;
	}
	
	public Game getGame()
	{
		return _game;
	}

	public Player getYourInfo()
	{
		return _yourInfo;
	}

	public Client.gameState getGameState()
	{
		return _gameState;
	}
	
	public void setTeam(int team)
	{
		_yourTeam = team;
	}
	
	/* Réception des de la partie données du serveur dédié uniquement a l'utilisation dans l'affichage et/ou effet sonores*/
	
	public void playerShootReceive(int x, int y)
	{
		_game.playerShootReceive(x, y);
	}
	
	public void MvtTargetReceive(int id, double x, double y, double z)
	{
		_game.MvtTargetReceive(id, x, y, z);
	}
	
	public void KillTargetReceive(int id)
	{
		_game.KillTargetReceive(id);
	}
	
	public void OutTargetReceive(int id)
	{
		_game.OutTargetReceive(id);
	}
	
	public void ChangeScoreReceive(String player, int score)
	{
		_game.ChangeScoreReceive(player, score);
	}
	
	public void PlayerTurnReceive(String player)
	{
		_game.PlayerTurnReceive(player);
	}
	
	public void EndGameReceive(int scoreTeam1, int scoreTeam2, int teamWin)
	{
		_game.EndGameReceive(scoreTeam1, scoreTeam2, teamWin);
		clearNetwork();
	}
	
	public void PosForShootReceive(int pos)
	{
		_game.PosForShootReceive(pos);
	}
	
	public void PullReceive()
	{
		_game.PullReceive();
	}
	
	public void MarkReceive()
	{
		_game.MarkReceive();
	}
	
	public void DoubleReceive()
	{
		_game.DoubleReceive();
	}
	
	public String getError()
	{
		return _error;
	}

	public void setError(String error)
	{
		_error = error;
	}

	public String getCurrentName()
	{
		return _currentName;
	}
	
	public void setCurrentName(String error)
	{
		_currentName = error;
	}

	public boolean getFlagChangeName()
	{
		return _flagChangeName;
	}
	
	public void setFlagChangeName(boolean value)
	{
		_flagChangeName = value;
	}
	
	public boolean getFlagQuitRoom()
	{
		if (_network == null)
			return true;
		return _flagQuitRoom;
	}
	
	public void setFlagQuitRoom(boolean value)
	{
		_flagQuitRoom = value;
	}
	
	public boolean getFlagQuitGame()
	{
		if (_network == null)
			return true;
		return _flagQuitGame;
	}
	
	public void setFlagQuitGame(boolean value)
	{
		_flagQuitGame = value;
	}
	
	public boolean getFlagLaunchGame()
	{
		return _flagLaunchGame;
	}
	
	public void setFlagLaunchGame(boolean value)
	{
		_flagLaunchGame = value;
	}

	public void fireReceive()
	{
		_game.fireReceive();
	}

	public int getScore()
	{
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore();
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore();
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore();
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore();
		return 0;
	}

	public int getScoreTeam()
	{
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore() +
				   _game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore();
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore() +
					   _game.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore();
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore() +
					   _game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore();
		if (_game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(_yourInfo.GetName()) == 0)
			return _game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore() +
					   _game.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore();
		return 0;
	}
	
	public GameRun.cameraState findViewStation()
	{
		if (_game.isPlayerTurn(_yourInfo.GetName()) == true)
		{
			switch (_game.getStation())
			{
			case 1 :
				return GameRun.cameraState.BASE_ONE;
			case 2 :
				return GameRun.cameraState.BASE_TWO;
			case 3 :
				return GameRun.cameraState.BASE_THREE;
			case 4 :
				return GameRun.cameraState.BASE_FOUR;
			case 5 :
				return GameRun.cameraState.BASE_FIVE;
			case 6 :
				return GameRun.cameraState.BASE_SIX;
			case 7 :
				return GameRun.cameraState.BASE_SEVEN;
			case 8 :
				return GameRun.cameraState.BASE_HEIGHT;
			}
		}
		return GameRun.cameraState.GLOBAL;
	}
	
	public GameRun.cameraState findStation()
	{
		switch (_game.getStation())
		{
		case 1 :
			return GameRun.cameraState.BASE_ONE;
		case 2 :
			return GameRun.cameraState.BASE_TWO;
		case 3 :
			return GameRun.cameraState.BASE_THREE;
		case 4 :
			return GameRun.cameraState.BASE_FOUR;
		case 5 :
			return GameRun.cameraState.BASE_FIVE;
		case 6 :
			return GameRun.cameraState.BASE_SIX;
		case 7 :
			return GameRun.cameraState.BASE_SEVEN;
		case 8 :
			return GameRun.cameraState.BASE_HEIGHT;
		}
		return GameRun.cameraState.GLOBAL;
	}

	public boolean isHost()
	{
		if (_network == null)
			return false;
		return _network.isHost();
	}
}