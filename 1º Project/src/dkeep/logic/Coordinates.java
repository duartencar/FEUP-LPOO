package dkeep.logic;

/**
 * Class Coordinates: represents the characters location in game.
 */
public class Coordinates
{
  private int x;
  private int y;

  /**
  * Coordinates default constructor
  */
  public Coordinates()
  {
    this.x = 0;

    this.y = 0;
  }

  /**
  * Coordinates constructor by individual int parameters
  * @param x {int} - x coordinate
  * @param y {int} - y coordinate
  */
  public Coordinates(int x, int y)
  {
    this.x = x;

    this.y = y;
  }

  /**
  * Returns x coordinate
  * @return x coordinate
  */
  public int getX()
  {
    return x;
  }

  /**
  * Returns y coordinate
  * @return y coordinate
  */
  public int getY()
  {
    return y;
  }
  
  /**
  * sets x coordinate to parameter x
  * @param x {int} - x coordinate
  */
  public void setX(int x)
  {
     this.x = x;
  }

  /**
  * sets y coordinate to parameter y
  * @param y {int} - y coordinate
  */
  public void setY(int y)
  {
     this.y = y;
  }

  /**
  * adds the x parameter to x coordinate
  * @param x {int} - value to add
  */
  public void addX(int x)
  {
    this.x += x;
  }

  /**
  * adds the y parameter to y coordinate
  * @param y {int} - value to add
  */
  public void addY(int y)
  {
    this.y += y;
  }

  /**
  * Compares two Coordinates
  * @param obj {Object} - coordinate to compare too
  * @returns true if they are equal, false if they dont
  */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Coordinates))
			return false;
		Coordinates other = (Coordinates) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

  /**
  * Clones this object and returns it
  * @return a clone Coordinate object
  */
  public Coordinates clone()
  {
    Coordinates clone = new Coordinates();

    clone.setX(this.getX());

    clone.setY(this.getY());

    return clone;
  }

	@Override
	public String toString()
	{
		return "(" + x + "," + y + ")";
	}
	
	/**
	  * determines if the coordinates given are side to side(either in the horizontal or vertical) with this character coordinates
	  * @param neighboor {Coordinates} - coordinates to check with the character location
	  * @return true if the coordinate given and this object coordinates are "neighboor", false otherwise
	  */
	public boolean isVerticalOrHorizontalNeighbor(Coordinates neighboor)
	{
		if(this.equals(neighboor))
			return true;
		else if(Math.abs(getX() - neighboor.getX()) == 1 && Math.abs(getY() - neighboor.getY()) == 0) //if they are side by side on the same line
			return true;
		else if(Math.abs(getX() - neighboor.getX()) == 0 && Math.abs(getY() - neighboor.getY()) == 1) //if they are side by side on the same column
			return true;
		
		return false;
	}
}
