import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;

public class GameRun extends BasicGameState implements ComponentListener
{
	private StateBasedGame stateBasedGame;
	private SoundManager _soundManager;
	private Image _backgroundImgBaseOne;
	private Image _backgroundImgBaseTwo;
	private Image _backgroundImgBaseThree;
	private Image _backgroundImgBaseFour;
	private Image _backgroundImgBaseFive;
	private Image _backgroundImgBaseSix;
	private Image _backgroundImgBaseSeven;
	private Image _backgroundImgBaseHeight;
	private Image _backgroundImgGlobal;
	private Image _pidgePullImg;
	private Image _pidgeMarkImg;
	private Image _deadPidgePull;
	private Image _deadPidgeMark;
	private Image _hunter;
	private Image _bullet;
	private MouseOverArea Fire;
	public enum cameraState {BASE_ONE, BASE_TWO, BASE_THREE, BASE_FOUR, BASE_FIVE, BASE_SIX, BASE_SEVEN, BASE_HEIGHT, GLOBAL;}
	cameraState _state;
	
	public GameRun(int state, SoundManager sm)
	{
		_soundManager = sm;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException
	{
		stateBasedGame = sbg;
		_backgroundImgBaseOne = new Image("Resources/Images/backgroundPost1.png");
		_backgroundImgBaseTwo = new Image("Resources/Images/backgroundPost2.png");
		_backgroundImgBaseThree = new Image("Resources/Images/backgroundPost3.png");
		_backgroundImgBaseFour = new Image("Resources/Images/backgroundPost4.png");
		_backgroundImgBaseFive = new Image("Resources/Images/backgroundPost5.png");
		_backgroundImgBaseSix = new Image("Resources/Images/backgroundPost6.png");
		_backgroundImgBaseSeven = new Image("Resources/Images/backgroundPost7.png");
		_backgroundImgBaseHeight = new Image("Resources/Images/backgroundPost8.png");
		_backgroundImgGlobal = new Image("Resources/Images/TopView.png");
		_pidgePullImg = new Image("Resources/Images/AngryPidge1.png");
		_pidgeMarkImg = new Image("Resources/Images/AngryPidge2.png");
		_deadPidgePull = new Image("Resources/Images/AngryPidgeDead1.png");
		_deadPidgeMark = new Image("Resources/Images/AngryPidgeDead2.png");
		_bullet = new Image("Resources/Images/Bullet.png");
		_hunter = new Image("Resources/Images/hunter.png");

		Game.setWidthTarget(_pidgePullImg.getWidth());
		Game.setHeightTarget(_pidgePullImg.getHeight());
		
	    Fire = new MouseOverArea(gc, new Image("Resources/Images/Fire.png"), 750, 550, this);
	    Fire.setNormalColor(new Color(0.7f, 0.7f, 0.7f,1f));
	    Fire.setMouseOverColor(new Color(1f,1f,1f,1f));
	    
	    _soundManager.LoadSound("shoot", "Resources/Sounds/shootSound.wav");
	  	_soundManager.LoadSound("prepare", "Resources/Sounds/prepareYourself.wav");
	  	_soundManager.LoadSound("fly", "Resources/Sounds/pigeonFly.wav");
	  	
		_state = cameraState.GLOBAL;
	}

	public int getID()
	{
		return 5;
	}
	
	@Override
	public void componentActivated(AbstractComponent source) {
		// TODO Auto-generated method stub

		if (source == Fire)
		{
			AngryPidge._client.clickFire();
			_soundManager.PlaySound("prepare");
		}

	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException
	{
		Input input = gc.getInput();

		if (input.isKeyPressed(Input.KEY_ESCAPE))
			sbg.enterState(0);
		if (input.isMousePressed(0) == true && AngryPidge._client.getGame().getFireDone() == true)
		{
			if (input.getMouseY() <= (AngryPidge.screenHeigth * 4 / 6))
				AngryPidge._client.ClickShoot(input.getMouseX(), input.getMouseY());
		}
		if (AngryPidge._client._game.GetEndGame() == false)
		{
			sbg.enterState(6);
		}
		updateWithNetwork(sbg);
	}

	public void updateWithNetwork(StateBasedGame sbg)
	{
		if (AngryPidge._client._game.GetEndGame() == false)
		{
			sbg.enterState(6);
		}
		if (AngryPidge._client._game.getShootEffect() == true)
		{
			AngryPidge._client._game.setShootEffect(false);
		    _soundManager.PlaySound("shoot");	
		}
		if (AngryPidge._client._game.getFireEffect() == true)
		{
			AngryPidge._client._game.setFireEffect(false);
			_soundManager.PlaySound("fly");
		}
		if (AngryPidge._client.getFlagQuitGame() == true)
		{
			stateBasedGame.enterState(6);
			AngryPidge._client.setFlagQuitGame(false);
		}
		
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException
	{		
		updateWithNetwork(sbg);
		drawBases(gc, sbg, g);
		drawScore(gc, sbg, g);
	}

	public void drawBases(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		switch (AngryPidge._client.findViewStation())
		{
		case BASE_ONE:
			drawBaseOne(gc, sbg, g);
			break;
		case BASE_TWO:
			drawBaseTwo(gc, sbg, g);
			break;
		case BASE_THREE:
			drawBaseThree(gc, sbg, g);
			break;
		case BASE_FOUR:
			drawBaseFour(gc, sbg, g);
			break;
		case BASE_FIVE:
			drawBaseFive(gc, sbg, g);
			break;
		case BASE_SIX:
			drawBaseSix(gc, sbg, g);
			break;
		case BASE_SEVEN:
			drawBaseSeven(gc, sbg, g);
			break;
		case BASE_HEIGHT:
			drawBaseHeight(gc, sbg, g);
			break;
		case GLOBAL:
			drawGlobal(gc, sbg, g);
			break;
		default:
			break;
		}
	}
	
	private void drawExtraForTurn(GameContainer gc, StateBasedGame sbg, Graphics g)
	{
		if (AngryPidge._client.getGame().getFireDone() == false)
			Fire.render(gc, g);
		else
		{
			int nbBullet = AngryPidge._client.getGame().getNbBullet();
			if (nbBullet < 10)
			{
				for (int i = 0; i < nbBullet; i++)
				{
					_bullet.draw(950 - (i * 50), 700, 1f);
				}
			}
			else
			{
				_bullet.draw(850, 700, 1f);
				g.setColor(Color.black);
				g.scale(1.8f, 1.8f);
				g.drawString(" x " + Integer.toString(nbBullet), 500, 385);
				g.scale((1 / 1.8f), (1 / 1.8f));
			}
		}
	}
	
	private void drawPidge(double x, double y, double z, boolean isPull)
	{
		if (isPull == true)
			_pidgePullImg.draw((float)x, (float)((AngryPidge.screenHeigth * 4 / 6) - y), (float)z);
		else
			_pidgeMarkImg.draw((float)x, (float)((AngryPidge.screenHeigth * 4 / 6) - y), (float)z);
	}
	
	public void drawBaseOne(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//________ON DESSINE LES DECORS__________________
		//je suppose qu'un simple background pour chaque vu suffira =)
		_backgroundImgBaseOne.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			if (target.get(i).getX() >= (Game.fireZoneWidth / 6) &&
					target.get(i).getX() <= (Game.fireZoneWidth / 2))
			{
				double x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 2 / 6)) * (target.get(i).getX() - (Game.fireZoneWidth / 6));
				double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
				double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 2);
	
				drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
			}
		}
		drawExtraForTurn(gc, sbg, g);
	}
	
	public void drawBaseTwo(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//________ON DESSINE LES DECORS__________________
		//je suppose qu'un simple background pour chaque vu suffira =)
		_backgroundImgBaseTwo.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			if (target.get(i).getX() >= (Game.fireZoneWidth / 7) &&
					target.get(i).getX() <= (Game.fireZoneWidth * 10 / 12))
			{
				double x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 29 / 42)) * (target.get(i).getX() - (Game.fireZoneWidth / 7));
				double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
				double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 4);
	
				drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
			}
		}
		drawExtraForTurn(gc, sbg, g);
	}
	
	public void drawBaseThree(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//________ON DESSINE LES DECORS__________________
		_backgroundImgBaseThree.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			if (target.get(i).getX() >= (Game.fireZoneWidth / 8) &&
					target.get(i).getX() <= (Game.fireZoneWidth * 2 / 3))
			{
				double x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 13 / 24)) * (target.get(i).getX() - (Game.fireZoneWidth / 8));
				double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
				double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 6);
	
				drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
			}
		}
		drawExtraForTurn(gc, sbg, g);
	}
	
	public void drawBaseFour(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{	
		//________ON DESSINE LES DECORS__________________
		_backgroundImgBaseFour.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			double x = (AngryPidge.screenWidth / (Game.fireZoneWidth)) * target.get(i).getX();
			double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
			double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 7);
	
			drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
		}
		drawExtraForTurn(gc, sbg, g);
	}
	
	public void drawBaseFive(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//________ON DESSINE LES DECORS__________________
		_backgroundImgBaseFive.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________	
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			if (target.get(i).getX() >= (Game.fireZoneWidth / 3) &&
					target.get(i).getX() <= (Game.fireZoneWidth * 7 / 8))
			{
				double x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 13 / 24)) * (target.get(i).getX() - (Game.fireZoneWidth / 3));
				double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
				double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 6);
	
				drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
			}
		}
		drawExtraForTurn(gc, sbg, g);
	}
	
	public void drawBaseSix(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//________ON DESSINE LES DECORS__________________
		//je suppose qu'un simple background pour chaque vu suffira =)
		_backgroundImgBaseSix.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			if (target.get(i).getX() >= (Game.fireZoneWidth * 2 / 12) &&
					target.get(i).getX() <= (Game.fireZoneWidth * 6 / 7))
			{
				double x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 29 / 42)) * (target.get(i).getX() - (Game.fireZoneWidth * 2 / 12));
				double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
				double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 4);
	
				drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
			}
		}
		drawExtraForTurn(gc, sbg, g);
	}
	
	public void drawBaseSeven(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//________ON DESSINE LES DECORS__________________
		//je suppose qu'un simple background pour chaque vu suffira =)
		_backgroundImgBaseSeven.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			if (target.get(i).getX() >= (Game.fireZoneWidth / 2) &&
					target.get(i).getX() <= (Game.fireZoneWidth * 5 / 6))
			{
				double x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 2 / 6)) * (target.get(i).getX() - (Game.fireZoneWidth / 2));
				double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
				double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 2);
	
				drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
			}
		}
		drawExtraForTurn(gc, sbg, g);
	}


	public void drawBaseHeight(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{	
		//________ON DESSINE LES DECORS__________________
		_backgroundImgBaseHeight.draw(0,0);
		
		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			if (target.get(i).getX() >= (Game.fireZoneWidth / 3) &&
					target.get(i).getX() <= (Game.fireZoneWidth * 2 / 3))
			{
				double x = (AngryPidge.screenWidth / (Game.fireZoneWidth * 1 / 3)) * (target.get(i).getX() - (Game.fireZoneWidth / 3));
				double y = ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight) * (target.get(i).getY() - 1);
				double z = (Game.fireZoneDepth / 5) / (target.get(i).getZ() + 2);
	
				drawPidge((float)x, (float)y, (float)z, target.get(i).is_fromPull());
			}
		}
		drawExtraForTurn(gc, sbg, g);
 	}

	public void drawGlobal(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		//VUE DU DESSUS
		//________ON DESSINE LES DECORS__________________
		//je suppose qu'un simple background pour chaque vu suffira =)
		_backgroundImgGlobal.draw(0,0);
		

		//__________ON DESSINE LES PIGEON_________________
		List<Target> target = AngryPidge._client.getGame().getTargets();
		for (int i = 0; i < target.size(); i++)
		{
			double x = (723 / (Game.fireZoneWidth + 30)) * target.get(i).getX();
			double y = (397 / (Game.fireZoneHeight + 30)) * target.get(i).getY();
			double z = 0.4;

			if (target.get(i).is_fromPull() == true)
				_pidgePullImg.draw((float)(x + 148), (float)(397 - y), (float)z);
			else
				_pidgeMarkImg.draw((float)(x + 148), (float)(397 - y), (float)z);
		}
		drawHunterAndShoot(gc, sbg, g);
		g.setColor(Color.darkGray);
		g.scale(1.5f, 1.5f);
		g.drawString(AngryPidge._client.getGame().getCurrentPlayerName() + " is playing.", 250, 10);
		g.scale((1.0f / 1.5f), (1.0f / 1.5f));
	}

	public void drawDeadPidge(GameContainer gc, StateBasedGame sbg, Graphics g, List<Target> list) throws SlickException 
	{
		for (int i = 0; i < list.size(); i++)
		{
			double x = (723 / (Game.fireZoneWidth + 30)) * list.get(i).getX() - (_deadPidgePull.getWidth() / 6); 
			double y = (397 / (Game.fireZoneHeight + 30)) * list.get(i).getY() + (_deadPidgePull.getHeight() / 6);
			double z = 0.4;

			if (list.get(i).is_fromPull() == true)
				_deadPidgePull.draw((float)(x + 148), (float)(397 - y), (float)z);
			else
				_deadPidgeMark.draw((float)(x + 148), (float)(397 - y), (float)z);
		}
	}
	
	public void drawShoot(GameContainer gc, StateBasedGame sbg, Graphics g, double xShooter, double yShooter) throws SlickException 
	{
		int i = 0;

		while (i < AngryPidge._client.getGame().getListShootDone().size())
		{
			ShootDone shoot = AngryPidge._client.getGame().getListShootDone().get(i);
			
			shoot.lessTemp();
			if (shoot.getTemp() > 0)
			{
				double destY, destX;
				if (shoot.getTouch() == false)
				{
					double x = 148 + (723 / (Game.fireZoneWidth + 30)) * shoot.getX();
					double y = 397 - (397 / (Game.fireZoneHeight + 30)) * shoot.getY();
					double distY = yShooter - y;
					double distX;
					if (xShooter >= x)
						distX = xShooter - x;
					else
						distX = x - xShooter;
					if (distX == 0)
					{
						destX = xShooter;
						destY = 0;
					}
					else
					{
						destY = yShooter;
						destX = xShooter;
						while (destY > 0 && destX > 0 && destX < AngryPidge.screenWidth)
						{
							if (xShooter > x)
								destX--;
							else
								destX++;
							destY -= (distY / distX);
						}
					}
				}
				else
				{
					destX = 148 + (((723 / (Game.fireZoneWidth + 30)) * shoot.getTarget().get(0).getX()));
					destY = 397 - (((397 / (Game.fireZoneHeight + 30)) * shoot.getTarget().get(0).getY()));
					drawDeadPidge(gc, sbg, g, shoot.getTarget());
				}
				Line toDraw = new Line((float)xShooter, (float)yShooter, (float)destX, (float)destY);
				g.setColor(Color.red);
				g.draw(toDraw);
				i++;
			}
			else
			{
				AngryPidge._client.getGame().getListShootDone().remove(i);
			}
		}
	}
	
	public void drawHunterAndShoot(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		cameraState camera = AngryPidge._client.findStation();
		
		switch (camera)
		{
		case BASE_ONE:
			_hunter.draw(65, 365, 0.5f);
			drawShoot(gc, sbg, g, (65 + (_hunter.getWidth() / 2)), 380);
			break;
		case BASE_TWO:
			_hunter.draw(120, 520, 0.5f);
			drawShoot(gc, sbg, g, (120 + (_hunter.getWidth() / 2)), 535);
			break;
		case BASE_THREE:
			_hunter.draw(270, 645, 0.5f);
			drawShoot(gc, sbg, g, (270 + (_hunter.getWidth() / 2)), 660);
			break;
		case BASE_FOUR:
			_hunter.draw(485, 680, 0.5f);
			drawShoot(gc, sbg, g, (485 + (_hunter.getWidth() / 2)), 695);
			break;
		case BASE_FIVE:
			_hunter.draw(695, 635, 0.5f);
			drawShoot(gc, sbg, g, (695 + (_hunter.getWidth() / 2)), 650);
			break;
		case BASE_SIX:
			_hunter.draw(845, 520, 0.5f);
			drawShoot(gc, sbg, g, (845 + (_hunter.getWidth() / 2)), 535);
			break;
		case BASE_SEVEN:
			_hunter.draw(900, 365, 0.5f);
			drawShoot(gc, sbg, g, (900 + (_hunter.getWidth() / 2)), 380);
			break;
		case BASE_HEIGHT:
			_hunter.draw(480, 365, 0.5f);
			drawShoot(gc, sbg, g, (480 + (_hunter.getWidth() / 2)), 380);
			break;
		case GLOBAL:
			break;
		default:
			break;
		}
	}
	
	public void drawScore(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException 
	{
		g.setColor(Color.darkGray);
		g.scale(1.5f, 1.5f);
		g.drawString("score equipe 1 : " + Integer.toString(AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore() +
														AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore()), 10, 10);
		g.drawString("score " + AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName() + " : " +
				Integer.toString(AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore()), 15, 35);
		g.drawString("score " + AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName() + " : " +
				Integer.toString(AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_ONE).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore()), 15, 55);

		g.drawString("score equipe 2 : " + Integer.toString(AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore() + 
														AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore()), 475, 10);
		g.drawString("score " + AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetName() + " : " +
				Integer.toString(AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_ONE).GetScore()), 500, 35);
		g.drawString("score " + AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetName() + " : " +
				Integer.toString(AngryPidge._client.getRoom().GetTeam(Room.TeamNumber.TEAM_TWO).GetPlayer(Team.PlayerType.PLAYER_TWO).GetScore()), 500, 55);
	}
	
	public void setCameraState(cameraState state)
	{
		_state = state;
	}
	
	
}
