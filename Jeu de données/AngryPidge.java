
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class AngryPidge extends StateBasedGame
{
	private SoundManager soundManager;
	
	public static final int idMainMenu = 0;
	public static final int idMenuPlay = 1;
	public static final int idMenuOptions = 2;
	public static final int idMenuCredits = 3;
	public static final int idGameRoom = 4;
	public static final int idGameRun = 5;
	public static final int idGameScore = 6;
	
	public static Client _client;
	public static GameScore _gameScore;
	public static int screenWidth;
	public static int screenHeigth;
	
	public AngryPidge(String name)
	{
		super(name);
		soundManager = new SoundManager();
		_client = new Client();
		screenWidth = 1024;
		screenHeigth = 768;
	}
	
	public void initStatesList(GameContainer gc) throws SlickException
	{
		_gameScore = new GameScore(idGameScore, soundManager);
		MenuPlay menuPlay = new MenuPlay(idMenuPlay, soundManager);
		this.addState(new GameMenu(idMainMenu, soundManager, menuPlay));
		this.addState(menuPlay);
		this.addState(new MenuOptions(idMenuOptions, soundManager));
		this.addState(new MenuCredits(idMenuCredits, soundManager));
		this.addState(new GameRoom(idGameRoom, soundManager, menuPlay));
		this.addState(new GameRun(idGameRun, soundManager));
		this.addState(_gameScore);
	}
	
	public static void main(String[] args)
	{
		try
		{
			AppGameContainer app = new AppGameContainer(new AngryPidge("AngryPidge"));
			app.setDisplayMode(screenWidth, screenHeigth, false);
			app.setShowFPS(false);
			app.setIcon("Resources/Images/angryPidgeIcon.png");
			app.start();
		}
		catch (SlickException e)
		{
			System.out.println("No Slick librairie find");
			//e.printStackTrace();
		}
	}
}
