
public class Team 
{
	public enum PlayerType 
	{
		PLAYER_ONE,
		PLAYER_TWO;
	}

	Player[] _players;
	int _nbPlayersInTeam;
	
	public Team(Room.TeamNumber teamNumber)
	{ 
		_players = new Player[2];
		_players[0] = new Player(PlayerType.PLAYER_ONE, teamNumber);
		_players[1] = new Player(PlayerType.PLAYER_TWO, teamNumber);
		_nbPlayersInTeam = 0;
	}
	
	public void AddScore(int score, PlayerType type)
	{
		_players[type.ordinal()].SetScore(score);
	}
	
	public int GetScore()
	{
		return _players[0].GetScore() + _players[1].GetScore();
	}
	
	public int GetPlayerScore(PlayerType type)
	{
		return _players[type.ordinal()].GetScore();
	}
	
	public Player GetPlayer(PlayerType type)
	{
		return _players[type.ordinal()];
	}
	
	public Player[] GetPlayerList()
	{
		return _players;
	}
	
	public boolean AddPlayer(String name) 
	{ 
		if (_nbPlayersInTeam == 2)
			return false;
		if (_players[0].GetName() == "")
			_players[0].SetName(name);
		else
			_players[_nbPlayersInTeam].SetName(name);
		_nbPlayersInTeam++;
		return true;
	}
	
	public boolean RemovePlayer(String name) 
	{ 
		if (_nbPlayersInTeam == 0)
			return false;
		int cnt = _nbPlayersInTeam;
		while (cnt > 0)
		{
			if (_players[cnt - 1].GetName() == name)
			{
				_players[cnt - 1].SetName("");
				_players[cnt - 1].SetScore(0);
				_nbPlayersInTeam--;
				return true;
			}
			cnt--;
		}
		return false;
	}
}
