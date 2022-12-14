import java.lang.String;

public class Player 
{
	private String _name;
	private int _score;
	private Room.TeamNumber _teamNumber;
	private Team.PlayerType _playerType;
	
	public Player(Team.PlayerType playerType, Room.TeamNumber teamNumber)
	{
		_score = 0; 
		_name = "";
		_teamNumber = teamNumber;
		_playerType = playerType;
	}
	
	public String GetName() 
	{ 
		return _name; 
	}
	
	public void SetName(String name)
	{ 
		_name = name;
	}
	
	public int GetScore() 
	{
		return _score;
	}
	
	public void SetScore(int score) 
	{
		_score = score;
	}
	
	public void SetTeamNumber(Room.TeamNumber teamNumber)
	{
		_teamNumber = teamNumber;
	}
	
	public Room.TeamNumber GetTeamNumber()
	{
		return _teamNumber;
	}
	
	public void SetPlayerType(Team.PlayerType playerType)
	{
		_playerType = playerType;
	}
	
	public Team.PlayerType GetPlayerType()
	{
		return _playerType;
	}
}
