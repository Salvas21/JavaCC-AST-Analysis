import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread
{
	public enum TargetFrom
	{
		PULL,
		MARK,
		DOUBLE,
	};
	
	public enum e_state
	{
		STATE_CHANGE_STATION,
		STATE_CHANGE_PLAYER,
		STATE_END,
		STATE_READY,
		STATE_SHOOT
	};
	
	// Information du champ de tir (zone opposer à la position du joueur par rapport a la ligne PULL-MARK).	
	public static  double fireZoneWidth = 350;
	public static double fireZoneHeight = 200;
	public static double fireZoneDepth = 100;

	// Information du pigeon.	
	public static double widthTarget = 40;
	public static double heightTarget = 40;
	
	@SuppressWarnings("unchecked")
	private List<TargetFrom>[] _targetData = new LinkedList[4];
	private e_state _state = e_state.STATE_CHANGE_PLAYER;
	private final int _waitTime = 10000;
	private long _time = 0;
	private Room _room = null;
	private int _station = 1;
	private int _playerTurn = 0;
	private final ReentrantLock _listMutex = new ReentrantLock();
	private List<Target> _targets = new LinkedList<Target>();
	private List<ShootDone> _listShootDone = new LinkedList<ShootDone>();
	private Client _client;
	private boolean _end;
	private int _shootDone;
	private boolean _fireDone;
	private boolean _shootEffect;
	private boolean _fireEffect;
	
	public Game(Room room, Client client)
	{
		_room = room;
		_client = client;
		_playerTurn = 0;
		_shootDone = 0;
		_fireDone = false;
		initTargetData();
		_end = true;
		_shootEffect = false;
		_fireEffect = false;
	}
	
	private void initTargetData()
	{
		int nbPull = 0;
		int nbMark = 0;
		int nbDouble = 0;
		List<Integer> types = new LinkedList<Integer>();
		for (int i = 0; i < 4; ++i)
			_targetData[i] = new LinkedList<TargetFrom>();
		for (int i = 0; i < 3; ++i)
			types.add(i);
		for (int i = 0; i < 8; ++i)
			switch (types.get((int)(types.size() * Math.random())))
			{
			case 0:
				_targetData[0].add(TargetFrom.PULL);
				if (nbPull++ == 3)
					types.remove(new Integer(0));
				break;
			case 1:
				_targetData[0].add(TargetFrom.MARK);
				if (nbMark++ == 3)
					types.remove(new Integer(1));
				break;
			case 2:
				_targetData[0].add(TargetFrom.DOUBLE);
				if (nbDouble++ == 2)
					types.remove(new Integer(2));
				break;
			}
		for (int i = 0; i < _targetData[0].size(); ++i)
			for (int j = 1; j < 4; ++j)
				_targetData[j].add((int)(Math.random() * (_targetData[j].size() + 1)),
						_targetData[0].get(i));
	}
	
	private void nextPlayer()
	{
		Date date = new Date();
		if (date.getTime() < _time)
			return ;
		_targets.clear();
		_listShootDone.clear();
		_state = e_state.STATE_CHANGE_PLAYER;
		_playerTurn++;
		if (_playerTurn == 4)
		{
			_station++;
			_playerTurn = 0; // (int)(Math.random() * 4);
			_state = e_state.STATE_CHANGE_STATION;
			_client.changeStation(_station);
		}
		_fireDone = false;
		_fireEffect = false;
		_client.changePlayerTurn(_room.GetTeam(Room.TeamNumber.values()[_playerTurn / 2]).GetPlayerList()[_playerTurn % 2].GetName());
	}
	
	public void Shoot(int xShoot, int yShoot)
	{
		if (_shootDone <= 0)
			return ;
		_shootDone--;
		_listMutex.lock();
		int i = 0;
		double width;
		double height;
		_listShootDone.add(new ShootDone(xShoot, yShoot, _station));
		_shootEffect = true;
		while (i < _targets.size())
		{
			double x = _targets.get(i).getX();
			double y = _targets.get(i).getY();
			double z = _targets.get(i).getZ();
			boolean canView = false;

			switch (_station)
			{
			case 1 :
				if (x >= (Game.fireZoneWidth / 6) && x <= (Game.fireZoneWidth / 2))
					canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 2 / 6)) * (x - (Game.fireZoneWidth / 6));
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 2);
				break;
			case 2 :
				if (x >= (Game.fireZoneWidth / 7) && x <= (Game.fireZoneWidth * 10 / 12))
					canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 29 / 42)) * (x - (Game.fireZoneWidth / 7));
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 4);
				break;
			case 3 :
				if (x >= (Game.fireZoneWidth / 8) && x <= (Game.fireZoneWidth * 2 / 3))
					canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 13 / 24)) * (x - (Game.fireZoneWidth / 8));
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 6);
				break;
			case 4 :
				canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth)) * x;
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 7);
				break;
			case 5 :
				if (x >= (Game.fireZoneWidth / 3) && x <= (Game.fireZoneWidth * 7 / 8))
					canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 13 / 24)) * (x - (Game.fireZoneWidth / 3));
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 6);
				break;
			case 6 :
				if (x >= (Game.fireZoneWidth * 2 / 12) && x <= (Game.fireZoneWidth * 6 / 7))
					canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 29 / 42)) * (x - (Game.fireZoneWidth * 2 / 12));
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 4);
				break;
			case 7 :
				if (x >= (Game.fireZoneWidth / 2) && x <= (Game.fireZoneWidth * 5 / 6))
					canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 2 / 6)) * (x - (Game.fireZoneWidth / 2));
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 2);
				break;
			case 8 :
				if (x >= (Game.fireZoneWidth / 3) && x <= (Game.fireZoneWidth * 2 / 3))
					canView = true;
				x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 1 / 3)) * (x - (Game.fireZoneWidth / 3));
				y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (y - 1);
				z = (Game.fireZoneDepth / 5) / (z + 2);
				break;
			default :
				break;
			}
			width = widthTarget * z;
			height = heightTarget * z;
			y = (float)(AngryPidge.screenHeigth * 4 / 6) - y;

			if (canView == true && (xShoot >= x && xShoot <= (x + width)) &&
					(yShoot >= y && yShoot <= (y + height)))
			{
				 _client.killTarget(i);
				 _listShootDone.get(_listShootDone.size() - 1).addTarget(_targets.get(i));
                 _targets.remove(_targets.get(i));
                 Player player = _room.GetTeam(Room.TeamNumber.values()[_playerTurn / 2]).GetPlayerList()[_playerTurn % 2];
                 player.SetScore(player.GetScore() + 1);
                 _client.changeScoreTeam(player.GetScore(), _room.GetTeam(Room.TeamNumber.values()[_playerTurn / 2]).GetPlayerList()[_playerTurn % 2].GetName());
			}
			else
				i++;
		}
		if (_targets.size() == 0)
		{
			_state = e_state.STATE_END;
			Date date = new Date();
			_time = date.getTime() + 1000;
		}
		_listMutex.unlock();
	}
	
	public void Fire()
	{
		_fireDone = true;
		_fireEffect = true;
		createTarget();
		_time = 0;
	}
	
	private void createTarget()
	{
		_state = e_state.STATE_SHOOT;
		_targets.clear();
		switch (_targetData[_playerTurn].get(_station - 1))
		{
		case PULL :
			_targets.add(new Target(true));
			_client.Fire(false, true);
			_shootDone = 1;
			break;
		case MARK :
			_targets.add(new Target(false));
			_client.Fire(false, false);
			_shootDone = 1;
			break;
		case DOUBLE :
			_targets.add(new Target(true));
			_targets.add(new Target(false));
			_client.Fire(true, true);
			_shootDone = 2;
			break;
		}
	}
	
	private void manageTarget()
	{
		Date date = new Date();
		if (date.getTime() < _time)
			return ;
		_time = date.getTime() + 17; // ~60FPS
		//_time = date.getTime() + 60; // ~60FPS
		_listMutex.lock();
		int i = 0;
		while (i < _targets.size())
		{
			Target target = _targets.get(i);
			target.updatePosition();
			if (target.getX() < 0 || target.getY() < 0 || target.getX() > Game.fireZoneWidth
					|| target.getY() > Game.fireZoneHeight)
			{
				_targets.remove(target);
				_client.outTarget(i);
			}
			else
			{
				_client.mvtTarget(i++, target.getX(), target.getY(), target.getZ());
				_client.mvtTarget(i++, target.getX(), target.getY(), target.getZ());
			}
		}
		if (_targets.size()== 0)
			{
				_state = e_state.STATE_END;
				_time = date.getTime() + 3000;
			}
		_listMutex.unlock();
	}
	
	private void waiting()
	{
		Date date = new Date();
		if (_time < date.getTime())
		{
			if (_state == e_state.STATE_READY)
			{
				Fire();
				_state = e_state.STATE_SHOOT;
			}
			else
			{
				_time = date.getTime() + _waitTime;
				_state = e_state.STATE_READY;
			}
		}
	}
	
	public String getCurrentPlayerName()
	{
		return  _room.GetTeam(Room.TeamNumber.values()[_playerTurn / 2]).GetPlayerList()[_playerTurn % 2].GetName();
	}
	
	public int getPlayerTurn()
	{
		return _playerTurn;
	}
	
	public e_state get_state()
	{
		return _state;
	}

	public int getStation()
	{
		return _station;
	}

	public List<Target> getTargets()
	{
		return _targets;
	}
	
	public void run()
	{
		_end = true;
		_client.changeStation(1);
		_client.changePlayerTurn(_room.GetTeam(Room.TeamNumber.values()[_playerTurn / 2]).GetPlayerList()[_playerTurn % 2].GetName());
		while (_station != 9 && _end == true)
		{
			switch (_state)
			{
			case STATE_SHOOT:
				manageTarget();
				break;
			case STATE_END:
				nextPlayer();
				break;
			default :
				waiting();
				break;
			}
		}
		int score1 = _room.GetTeam(Room.TeamNumber.TEAM_ONE).GetScore();
		int score2 = _room.GetTeam(Room.TeamNumber.TEAM_TWO).GetScore();
		int teamWin;
		if (score1 > score2)
			teamWin = 1;
		else if (score2 > score1)
			teamWin = 2;
		else
			teamWin = 0;
		AngryPidge._gameScore.SetScoreTeam1(score1);
		AngryPidge._gameScore.SetScoreTeam2(score2);
		AngryPidge._gameScore.SetTeamWin(teamWin);
		_end = false;
		_client.sendEndGame(score1, score2, teamWin);
	}
	
	public void endGame()
	{
		_end = false;
	}
	
	/* Réception des de la partie données du serveur dédié uniquement a l'utilisation dans l'affichage et/ou effet sonores*/

	public boolean getShootDone()
	{
		return (_shootDone > 0 ? false : true);
	}
	
	public boolean getFireDone()
	{
		return _fireDone;
	}
	
	public Room getRoom()
	{
		return _room;
	}

	public void fireReceive()
	{
		_fireDone = true;
		_fireEffect = true;
	}
	
	public void playerShootReceive(int x, int y)
	{
		if (_shootDone > 0)
			_shootDone--;
		_listShootDone.add(new ShootDone(x, y, _station));
		_shootEffect = true;
	}
	
	public void EndGameReceive(int scoreTeam1, int scoreTeam2, int teamWin)
	{
		AngryPidge._gameScore.SetScoreTeam1(scoreTeam1);
		AngryPidge._gameScore.SetScoreTeam2(scoreTeam2);
		AngryPidge._gameScore.SetTeamWin(teamWin);
		_end = false;
	}
	
	public void MvtTargetReceive(int id, double x, double y, double z)
	{
		if (id < _targets.size())
		{
			_targets.get(id).setX(x);
			_targets.get(id).setY(y);
			_targets.get(id).setZ(z);
		}
	}
	
	public void KillTargetReceive(int id)
	{
		if (id < _targets.size())
		{
			if (_listShootDone.size() > 0)
				_listShootDone.get(_listShootDone.size() - 1).addTarget(_targets.get(id));
			_targets.remove(_targets.get(id));
		}
	}
	
	public void OutTargetReceive(int id)
	{
		if (id < _targets.size())
			_targets.remove(_targets.get(id));
	}
	
	public void ChangeScoreReceive(String player, int score)
	{
		if (_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(player) == 0)
			_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).SetScore(score);
		if (_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(player) == 0)
			_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).SetScore(score);
		if (_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(player) == 0)
			_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).SetScore(score);
		if (_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(player) == 0)
			_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).SetScore(score);
	}
	
	public void PlayerTurnReceive(String player)
	{
		_targets.clear();
		_listShootDone.clear();
		_fireDone = false;
		_fireEffect = false;
		if (_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(player) == 0)
			_playerTurn = 0;
		if (_room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(player) == 0)
			_playerTurn = 1;
		if (_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(player) == 0)
			_playerTurn = 2;
		if (_room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(player) == 0)
			_playerTurn = 3;
	}
	
	public boolean isPlayerTurn(String player)
	{
		if (_playerTurn == 0 && _room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(player) == 0)
			return true;
		if (_playerTurn == 1 && _room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(player) == 0)
			return true;
		if (_playerTurn == 2 && _room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName().compareTo(player) == 0)
			return true;
		if (_playerTurn == 3 && _room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName().compareTo(player) == 0)
			return true;
		return false;
	}
	
	public void PosForShootReceive(int pos)
	{
		_station = pos;
	}
	
	public void PullReceive()
	{
		_targets.clear();
		_targets.add(new Target(true));
		_shootDone = 1;
		_fireDone = true;
	}
	
	public void MarkReceive()
	{
		_targets.clear();
		_targets.add(new Target(false));
		_shootDone = 1;
		_fireDone = true;
	}
	
	public void DoubleReceive()
	{
		_targets.clear();
		_targets.add(new Target(true));
		_targets.add(new Target(false));
		_shootDone = 2;
		_fireDone = true;
	}
	
	public static void setWidthTarget(double width)
	{
		Game.widthTarget = width;
	}

	public static void setHeightTarget(double height)
	{
		Game.heightTarget = height;
	}
	
	public boolean GetEndGame()
	{
		return _end;
	}
	
	public int getNbBullet()
	{
		return _shootDone;
	}

	public void setShootEffect(boolean effect)
	{
		_shootEffect = effect;
	}
	
	public boolean getFireEffect()
	{
		return _fireEffect;
	}
	public void setFireEffect(boolean effect)
	{
		_fireEffect = effect;
	}
	
	public boolean getShootEffect()
	{
		return _shootEffect;
	}
	
	public List<ShootDone> getListShootDone()
	{
		return _listShootDone;
	}

	public String toString()
	{
		String send = "";
		String name = _room.GetTeam(Room.TeamNumber.values()[_playerTurn / 2]).GetPlayerList()[_playerTurn % 2].GetName();
		
		send += "Station = " + Integer.toString(_station) + "  -  PlayerTurn : " + name;
		for (int i = 0; i < _targets.size(); i++)
		{
			send += " \n" + Integer.toString(i) + "e Target";
			send += " - pos : " + Integer.toString((int)_targets.get(i).getX()) + "/" + Integer.toString((int)_targets.get(i).getY()) + "/" + Integer.toString((int)_targets.get(i).getZ());
		}
		send += "\n";
		return send;
	}
}
