import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuPlay extends BasicGameState implements ComponentListener
{
	private StateBasedGame stateBasedGame;
	//private SoundManager soundManager;
	
	private Image backgroundImg;
	private Image warningImg;
	
	private TextField joinIPTextField;
	private TextField joinPortTextField;
	private TextField createPortTextField;
	
	private MouseOverArea joinGame;
	private MouseOverArea createGame;
	private MouseOverArea backMenu;
	
	boolean isJoinWarning = false;
	boolean isCreateWarning = false;
	
	public MenuPlay(int state, SoundManager sm)
	{
		//soundManager = sm;
	}
	
	public int getID()
	{
		return 1;
	}
	
	public void reset()
	{
		isJoinWarning = false;
		isCreateWarning = false;
		AngryPidge._client.setFlagQuitRoom(false);
	}
	
	@Override
	public void componentActivated(AbstractComponent source)
	{
		if (source == joinGame)
		{
			try
			{
				if (AngryPidge._client.joinRoom(joinIPTextField.getText(), Integer.decode(joinPortTextField.getText())) == false)
				{
					isJoinWarning = true;
				}
				else
					stateBasedGame.enterState(4);
			}
			catch (NumberFormatException e)
			{
				isJoinWarning = true;
			}
		}
		

		if (source == createGame)
		{
			try
			{
				if (AngryPidge._client.createRoom(Integer.decode(createPortTextField.getText())) == false)
				{
					isCreateWarning = true;
				}
				else
					stateBasedGame.enterState(4);
			}
			catch (NumberFormatException e){
				isCreateWarning = true;
			}
		}
		if (source == backMenu)
			stateBasedGame.enterState(0);
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		stateBasedGame = sbg;
		backgroundImg = new Image("Resources/Images/menuBackground2.png");
		warningImg = new Image("Resources/Images/warning.png");
		
		joinIPTextField = new TextField(gc, gc.getDefaultFont(), 330, 410, 150, 30);
		joinPortTextField = new TextField(gc, gc.getDefaultFont(), 330, 330, 150, 30);
		createPortTextField = new TextField(gc, gc.getDefaultFont(), 820, 330, 150, 30);
		
		joinIPTextField.setText("127.0.0.1");
		joinPortTextField.setText("4242");
		createPortTextField.setText("4242");
		
		joinIPTextField.setMaxLength(15);
		joinPortTextField.setMaxLength(5);
		createPortTextField.setMaxLength(5);
		
		joinIPTextField.setBorderColor(Color.black);
		joinPortTextField.setBorderColor(Color.black);
		createPortTextField.setBorderColor(Color.black);
		
	    joinGame = new MouseOverArea(gc, new Image("Resources/Images/joinGame.png"), 300, 550, this);
	    joinGame.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    joinGame.setMouseOverColor(new Color(1f,1f,1f,1f));
	    
	    createGame = new MouseOverArea(gc, new Image("Resources/Images/createGame.png"), 750, 550, this);
	    createGame.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    createGame.setMouseOverColor(new Color(1f,1f,1f,1f));
	    
	    backMenu = new MouseOverArea(gc, new Image("Resources/Images/back.png"), 70, 650, this);
	    backMenu.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    backMenu.setMouseOverColor(new Color(1f,1f,1f,1f));
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			sbg.enterState(0);
		
		if (joinIPTextField.hasFocus() || joinPortTextField.hasFocus())
			isJoinWarning = false;
		if (createPortTextField.hasFocus())
			isCreateWarning = false;
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		backgroundImg.draw(0,0);
		if (isJoinWarning)
		{
			warningImg.draw(150, 470);
			gc.getDefaultFont().drawString(230, 480, AngryPidge._client.getError(), Color.red);
		}
		if (isCreateWarning)
		{
			warningImg.draw(560, 470);
			gc.getDefaultFont().drawString(630, 480, AngryPidge._client.getError(), Color.red);
		}
		g.setColor(Color.white);
		joinIPTextField.render(gc, g);
		joinPortTextField.render(gc, g);
		createPortTextField.render(gc, g);
		joinGame.render(gc, g);
		createGame.render(gc, g);
		backMenu.render(gc, g);
	}
	
	public void setIsJoinWarning(boolean value)
	{
		isJoinWarning = value;
	}
}