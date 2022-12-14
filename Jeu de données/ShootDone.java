import java.util.LinkedList;
import java.util.List;


public class ShootDone
{
	private double _x;
	private double _y;
	private boolean _touch;
	private List<Target> _targets = new LinkedList<Target>();
	public int _temp;
	
	public ShootDone(double x, double y, int _station)
	{
		switch (_station)
		{
		case 1 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth * 2 / 6)) + (Game.fireZoneWidth / 6) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		case 2 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth * 29 / 42)) + (Game.fireZoneWidth / 7) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		case 3 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth * 13 / 24)) + (Game.fireZoneWidth / 8) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		case 4 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth)) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		case 5 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth * 13 / 24)) + (Game.fireZoneWidth / 3) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		case 6 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth * 29 / 42)) + (Game.fireZoneWidth * 2 / 12) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		case 7 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth * 2 / 6)) + (Game.fireZoneWidth / 2) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		case 8 :
			_x = x / (AngryPidge.screenWidth / (Game.fireZoneWidth * 1 / 3)) + (Game.fireZoneWidth / 3) - 1;
			_y = (y + (AngryPidge.screenHeigth * 4 / 6)) / ((AngryPidge.screenHeigth * 4 / 6) / Game.fireZoneHeight);
			break;
		default :
			break;
		}
		_temp = 300;
		_touch = false;
	}
	
	public void addTarget(Target targetTouched)
	{
		_touch = true;
		_targets.add(targetTouched);
	}
	
	public void lessTemp()
	{
		_temp--;
	}
	
	public int getTemp()
	{
		return _temp;
	}
	
	public double getX()
	{
		return _x;
	}

	public double getY()
	{
		return _y;
	}

	public boolean getTouch()
	{
		return _touch;
	}
	
	public List<Target> getTarget()
	{
		return _targets;
	}
	
}
