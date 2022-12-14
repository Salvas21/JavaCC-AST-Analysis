
public class Room 
{
	public enum TeamNumber 
	{
		TEAM_ONE,
		TEAM_TWO;
	}
	
	private Team _teams[];
	public boolean _isReadyToRun = false;
	
	public Room()
	{
		_teams = new Team[2];
		_teams[0] = new Team(TeamNumber.TEAM_ONE);
		_teams[1] = new Team(TeamNumber.TEAM_TWO);
	}
	
	public boolean AddPlayer(String name, TeamNumber teamNumber)
	{
		return _teams[teamNumber.ordinal()].AddPlayer(name); 
	}	
	
	public boolean RemovePlayer(String name, TeamNumber teamNumber)
	{
		return _teams[teamNumber.ordinal()].RemovePlayer(name); 
	}
	
	public boolean ReadyToRun()
	{
		return _isReadyToRun; 
	}
	
	public Team GetTeam(TeamNumber teamNumber)
	{
		return _teams[teamNumber.ordinal()];
	}
	
	public Team[] GetTeamList() 
	{ 
		return _teams; 
	}
	
}
