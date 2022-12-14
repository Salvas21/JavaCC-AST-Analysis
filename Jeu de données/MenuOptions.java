import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuOptions extends BasicGameState implements ComponentListener
{
	private StateBasedGame stateBasedGame;
	private SoundManager soundManager;
	
	private Image backgroundImg;
	private ArrayList<Image> volumeImg;
	
	int soundImgcpt = 3;
	int songsImgcpt = 3;
	
	private MouseOverArea soundsVolume;
	private MouseOverArea songsVolume;
	private MouseOverArea backMenu;
	
	public MenuOptions(int state, SoundManager sm)
	{
		soundManager = sm;
	}
	
	public int getID()
	{
		return 2;
	}
	
	public void componentActivated(AbstractComponent source)
	{
		if (source == soundsVolume)
		{
			soundImgcpt++;
			soundImgcpt %= 4;
			soundsVolume.setNormalImage(volumeImg.get(soundImgcpt));
			soundsVolume.setMouseOverImage(volumeImg.get(soundImgcpt));
			soundsVolume.setMouseDownImage(volumeImg.get(soundImgcpt));
			soundManager.SetSoundsVolume(soundImgcpt * 0.35f);
		}
		
		if (source == songsVolume)
		{
			songsImgcpt++;
			songsImgcpt %= 4;
			songsVolume.setNormalImage(volumeImg.get(songsImgcpt));
			songsVolume.setMouseOverImage(volumeImg.get(songsImgcpt));
			songsVolume.setMouseDownImage(volumeImg.get(songsImgcpt));
			soundManager.SetMusicsVolume(songsImgcpt * 0.35f);
		}
		
		if (source == backMenu)
		{
			stateBasedGame.enterState(0);
		}
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		stateBasedGame = sbg;
		backgroundImg = new Image("Resources/Images/menuBackground3.png");
		volumeImg = new ArrayList<Image>();
		
		volumeImg.add(new Image("Resources/Images/sound0.png"));
		volumeImg.add(new Image("Resources/Images/sound1.png"));
		volumeImg.add(new Image("Resources/Images/sound2.png"));
		volumeImg.add(new Image("Resources/Images/sound3.png"));
		
		soundsVolume = new MouseOverArea(gc, volumeImg.get(3), 500, 270, this);
		soundsVolume.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		soundsVolume.setMouseOverColor(new Color(1f,1f,1f,1f));
		
		songsVolume = new MouseOverArea(gc, volumeImg.get(3), 500, 370, this);
		songsVolume.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
		songsVolume.setMouseOverColor(new Color(1f,1f,1f,1f));
		
		backMenu = new MouseOverArea(gc, new Image("Resources/Images/back.png"), 70, 650, this);
	    backMenu.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    backMenu.setMouseOverColor(new Color(1f,1f,1f,1f));
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException
	{
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_ESCAPE))
			sbg.enterState(0);
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException
	{
		backgroundImg.draw();
		soundsVolume.render(gc, g);
		songsVolume.render(gc, g);
		backMenu.render(gc, g);
	}
}