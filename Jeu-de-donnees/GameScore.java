import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameScore  extends BasicGameState implements ComponentListener
{
	private SoundManager _soundManager;
	private Image _backgroundImg;
	private Image _cup;
	private int _id;
	private MouseOverArea _backMenu;
	StateBasedGame _stateBasedGame;
	private int _scoreTeam1;
	private int _scoreTeam2;
	private int _teamWin;
	
	public GameScore(int idgamescore, SoundManager soundManager) {
		_id = idgamescore;
		_soundManager = soundManager;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame gb) throws SlickException
	{
		_soundManager.StopAllMusic();
		_soundManager.PlaySound("score");	
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame gb)
			throws SlickException {
		_backgroundImg = new Image("Resources/Images/backgroundEndGame.png");
		_cup = new Image("Resources/Images/cup.png");
		_backMenu = new MouseOverArea(gc, new Image("Resources/Images/back.png"), 70, 650, this);
		_backMenu.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		_backMenu.setMouseOverColor(new Color(1f,1f,1f,1f));
		_stateBasedGame = gb;
		_scoreTeam1 = 0;
		_scoreTeam2 = 0;
		_teamWin = 0;
		_soundManager.LoadSound("score", "Resources/Sounds/scoreSound.wav");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame gb, Graphics g)
			throws SlickException 
	{		
		_backgroundImg.draw(0,0);
		_backMenu.render(gc, g);
		drawScoreTab(gc, gb, g);
	}

	public void drawScoreTab(GameContainer gc, StateBasedGame gb, Graphics g)
	{
		Player team1Pl1 = AngryPidge._client._room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE);
		Player team1Pl2 = AngryPidge._client._room.GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO);
		Player team2Pl1 = AngryPidge._client._room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE);
		Player team2Pl2 = AngryPidge._client._room.GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO);
		
		if (_teamWin == 1)
			_cup.draw(225, 120);
		if (_teamWin == 2)
			_cup.draw(630, 120);
		if (_teamWin == 0)
		{
			_cup.draw(225, 120);
			_cup.draw(630, 120);
		}

		g.setColor(Color.yellow);
		g.scale(1.7f, 1.7f);
		g.setAntiAlias(true);
		
		int x, y, marging;
		x = gc.getScreenWidth() / 8 - 35;
		y = gc.getScreenHeight() / 8 - 6;
		marging = 100;
		
		
		g.drawString(team1Pl1.GetName(), x - 70, y + 95);
		g.drawString(team1Pl2.GetName(), x - 70, y + 155);
		
		g.drawString(Integer.toString(team1Pl1.GetScore()), x + marging - 25, y + 95);
		g.drawString(Integer.toString(team1Pl2.GetScore()), x + marging - 25, y + 155);
		g.drawString(Integer.toString(_scoreTeam1), x + marging - 25, y + 203);
		
		x = gc.getScreenWidth() / 4 + 70 - 40;
		y = gc.getScreenHeight() / 8 - 6;
		g.drawString(team2Pl1.GetName(), x - 100, y + 95);
		g.drawString(team2Pl2.GetName(), x - 100, y + 155);	
		g.drawString(Integer.toString(team2Pl1.GetScore()), x + marging - 55, y + 95);
		g.drawString(Integer.toString(team2Pl2.GetScore()), x + marging - 55, y + 155);
		g.drawString(Integer.toString(_scoreTeam2), x + marging - 55, y + 203);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame gb, int arg2)
			throws SlickException {
		
		
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		if (source == _backMenu)
			_stateBasedGame.enterState(0);
		_soundManager.PlayMusic("menuSong");
	}

	@Override
	public int getID() 
	{
		return _id;
	}

	public void SetScoreTeam1(int score)
	{
		_scoreTeam1 = score;
	}
	
	public void SetScoreTeam2(int score)
	{
		_scoreTeam2 = score;
	}
	
	public void SetTeamWin(int team)
	{
		_teamWin = team;
	}
	
	public int GetScoreTeam1()
	{
		return _scoreTeam1;
	}
	
	public int GetScoreTeam2()
	{
		return _scoreTeam2;
	}
	
	public int GetTeamWin()
	{
		return _teamWin;
	}
}
