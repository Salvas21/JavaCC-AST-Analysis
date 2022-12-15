import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameMenu extends BasicGameState implements ComponentListener
{
	private StateBasedGame stateBasedGame;
	private GameContainer gameContainer;
	private SoundManager soundManager;
	private Image backgroundImg;
	private Image titleImg;
	private Image playImg;
	private Image optionsImg;
	private Image creditsImg;
	private Image quitImg;
	
	private MouseOverArea play;
	private MouseOverArea options;
	private MouseOverArea credits;
	private MouseOverArea quit;
	private MenuPlay _menuPlay;
	
	public GameMenu(int state, SoundManager sm, MenuPlay menuPlay)
	{
		soundManager = sm;
		_menuPlay = menuPlay;
	}
	
	public int getID()
	{
		return 0;
	}
	
	@Override
	public void componentActivated(AbstractComponent source)
	{
		if (source == play)
		{
			_menuPlay.reset();
			stateBasedGame.enterState(1);
		}
		if (source == options)
			stateBasedGame.enterState(2);
		if (source == credits)
			stateBasedGame.enterState(3);
		if (source == quit)
			gameContainer.exit();
			
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		stateBasedGame = sbg;
		gameContainer = gc;
		backgroundImg = new Image("Resources/Images/menuBackground.png");
		titleImg = new Image("Resources/Images/menuTitle.png");
		playImg = new Image("Resources/Images/menuPlay.png");
		optionsImg = new Image("Resources/Images/menuOptions.png");
		creditsImg = new Image("Resources/Images/menuCredits.png");
		quitImg = new Image("Resources/Images/menuQuit.png");
		
		gc.setMouseCursor("Resources/Images/sightIcon.png", 25, 25);
		
		soundManager.LoadMusic("menuSong", "Resources/Songs/Menu_Song.wav");
		soundManager.SetMusicsVolume(1);
		
		play = new MouseOverArea(gc, playImg, gc.getWidth()/2 - playImg.getWidth()/2, 200, this);
		play.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		play.setMouseOverColor(new Color(1f,1f,1f,1f));
		
		options = new MouseOverArea(gc, optionsImg, gc.getWidth()/2 - optionsImg.getWidth()/2, 300, this);
		options.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		options.setMouseOverColor(new Color(1f,1f,1f,1f));
		
		credits = new MouseOverArea(gc, creditsImg, gc.getWidth()/2 - creditsImg.getWidth()/2, 400, this);
		credits.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		credits.setMouseOverColor(new Color(1f,1f,1f,1f));
		
		quit = new MouseOverArea(gc, quitImg, gc.getWidth()/2 - quitImg.getWidth()/2, 500, this);
		quit.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		quit.setMouseOverColor(new Color(1f,1f,1f,1f));
		
		soundManager.PlayMusic("menuSong", true);
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		Input input = gc.getInput();
	
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			gc.exit();
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		backgroundImg.draw(0,0);
		titleImg.draw(gc.getWidth()/2 - titleImg.getWidth()/2, gc.getHeight()/2 - titleImg.getHeight()*2);
		
		play.render(gc, g);
		options.render(gc, g);
		credits.render(gc, g);
		quit.render(gc, g);
	}
}