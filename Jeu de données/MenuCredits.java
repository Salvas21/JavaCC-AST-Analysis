
import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class MenuCredits extends BasicGameState  implements ComponentListener
{
	private MouseOverArea _backMenu;
	private Image _backgroundImg;
	StateBasedGame _stateBasedGame;
	
	public MenuCredits(int state, SoundManager sm)
	{
		
	}
	
	public int getID()
	{
		return 3;
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		_backgroundImg = new Image("Resources/Images/backgroundCredit.png");
		_backMenu = new MouseOverArea(gc, new Image("Resources/Images/back.png"), 20, 690, this);
		_backMenu.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		_backMenu.setMouseOverColor(new Color(1f,1f,1f,1f));
		_stateBasedGame = sbg;
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			sbg.enterState(0);
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		_backgroundImg.draw(0,0);
		_backMenu.render(gc, g);
		
	}

	@Override
	public void componentActivated(AbstractComponent source) {
		if (source == _backMenu)
			_stateBasedGame.enterState(0);
	}
}