import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameRoom extends BasicGameState implements ComponentListener
{
	private StateBasedGame stateBasedGame;
	//private SoundManager soundManager;
	
	private Image backgroundImg;
	
	private TextField nameTextField;
	
	private MouseOverArea quitMenu;
	private MouseOverArea launchMenu;
	private MouseOverArea changeTeamMenu;
	private MenuPlay _menuPlay;
	
	public GameRoom(int state, SoundManager sm, MenuPlay menuPlay)
	{
	//	soundManager = sm;
		_menuPlay = menuPlay;
	}
	
	public int getID()
	{
		return 4;
	}
	
	@Override
	public void componentActivated(AbstractComponent source)
	{
		if (source == quitMenu)
		{
			AngryPidge._client.quitRoom();
			AngryPidge._client.clearNetwork();
			stateBasedGame.enterState(0);
		}
		if (source == changeTeamMenu)
			AngryPidge._client.changeTeam();
			
		//APPEL DE L ECRAN DE GAMEPLAY - DEBUT DE LA PARTIE
		if (source == launchMenu && AngryPidge._client.isHost() == true)
		{
			AngryPidge._client.runGame();
		}
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		stateBasedGame = sbg;
		backgroundImg = new Image("Resources/Images/gameRoomBackground.png");

		nameTextField = new TextField(gc, gc.getDefaultFont(), 700, 220, 240, 30);
		nameTextField.setBorderColor(Color.black);
		nameTextField.setText(AngryPidge._client.getCurrentName());
		nameTextField.setMaxLength(25);
		
		quitMenu = new MouseOverArea(gc, new Image("Resources/Images/quitGame.png"), 70, 680, this);
	    quitMenu.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    quitMenu.setMouseOverColor(new Color(1f,1f,1f,1f));
	    
	    launchMenu = new MouseOverArea(gc, new Image("Resources/Images/launchGame.png"), 650, 680, this);
	    launchMenu.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    launchMenu.setMouseOverColor(new Color(1f,1f,1f,1f));
	    
	    changeTeamMenu = new MouseOverArea(gc, new Image("Resources/Images/changeTeam.png"), 700, 420, this);
	    changeTeamMenu.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    changeTeamMenu.setMouseOverColor(new Color(1f,1f,1f,1f));
		
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		Input input = gc.getInput();
	
		//Si indication deco, le faire ici
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			stateBasedGame.enterState(0);
		
		//Test
		if (nameTextField.hasFocus() == true && nameTextField.getText().isEmpty() == false && AngryPidge._client.getCurrentName() != nameTextField.getText())
			AngryPidge._client.changeName(nameTextField.getText());
	}
	
	public void updateWithNetwork()
	{
		if (AngryPidge._client.getFlagChangeName() == true)
		{
			nameTextField.setText(AngryPidge._client.getCurrentName());
			AngryPidge._client.setFlagChangeName(false);
		}
		if (AngryPidge._client.getFlagQuitRoom() == true)
		{
			_menuPlay.setIsJoinWarning(true);
			stateBasedGame.enterState(1);
			AngryPidge._client.setFlagQuitRoom(false);
		}
		if (AngryPidge._client.getFlagLaunchGame() == true)
		{
			AngryPidge._client.setFlagLaunchGame(false);
			stateBasedGame.enterState(5);
		}
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		updateWithNetwork();
		backgroundImg.draw(0,0);
		
		quitMenu.render(gc, g);
		if (AngryPidge._client.isHost() == true)
			launchMenu.render(gc, g);
		changeTeamMenu.render(gc, g);
		nameTextField.render(gc, g);
		
		gc.getDefaultFont().drawString(100, 210, "PLAYER 1:", Color.white);
		gc.getDefaultFont().drawString(100, 295, "PLAYER 2:", Color.white);
		gc.getDefaultFont().drawString(100, 465, "PLAYER 3:", Color.white);
		gc.getDefaultFont().drawString(100, 550, "PLAYER 4:", Color.white);

		gc.getDefaultFont().drawString(200, 210, AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName(), Color.white);
		gc.getDefaultFont().drawString(200, 295, AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName(), Color.white);
		gc.getDefaultFont().drawString(200, 465, AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName(), Color.white);
		gc.getDefaultFont().drawString(200, 550, AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName(), Color.white);
	}
}