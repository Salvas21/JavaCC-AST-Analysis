
public class Target
{
	private double _x;
	private double _y;
	private double _z;
	private boolean _fromPull;
	
	public Target(boolean fromPull)
	{
		_fromPull = fromPull;
		if (_fromPull == true)
		{
			_x = 0;
			_y = Game.fireZoneHeight / 3;
			_z = 10;
		}
		else
		{
			_x = Game.fireZoneWidth;
			_y = Game.fireZoneHeight / 6;
			_z = 10;
		}
	}

	public void updatePositionPull()
	{
		_x++;
		if (_y < (Game.fireZoneHeight * 2 / 3))
			_y += ((Game.fireZoneHeight * 2 / 3) / (Game.fireZoneWidth / 2));
		else
			_y += ((Game.fireZoneHeight * 1 / 6) / (Game.fireZoneWidth / 2));
		if (_z < (Game.fireZoneDepth * 2 / 3))
			_z += ((Game.fireZoneDepth * 2 / 3) / (Game.fireZoneWidth / 2));
		else
			_z += ((Game.fireZoneDepth / 6) / (Game.fireZoneWidth / 2));
	}
	
	public void updatePositionMark()
	{
		_x--;
		if (_y < (Game.fireZoneHeight / 2))
			_y += ((Game.fireZoneHeight / 2) / (Game.fireZoneWidth / 2));
		else
			_y += ((Game.fireZoneHeight * 2 / 6) / (Game.fireZoneWidth / 2));
		if (_z < (Game.fireZoneDepth * 2 / 3))
			_z += ((Game.fireZoneDepth * 2 / 3) / (Game.fireZoneWidth / 2));
		else
			_z += ((Game.fireZoneDepth / 6) / (Game.fireZoneWidth / 2));
	}
	
	public void updatePosition()
	{
		if (_fromPull == true)
			updatePositionPull();
		else
			updatePositionMark();
	}


	public boolean is_fromPull()
	{
		return _fromPull;
	}
	
	public double getX()
	{
		return _x;
	}

	public void setX(double x)
	{
		_x = x;
	}

	public double getY()
	{
		return _y;
	}

	public void setY(double y)
	{
		_y = y;
	}

	public double getZ()
	{
		return _z;
	}

	public void setZ(double z)
	{
		_z = z;
	}
}
