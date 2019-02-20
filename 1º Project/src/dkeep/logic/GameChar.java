package dkeep.logic;

/**
 * Represents a character in the game
 * @see Coordinates
 */
public class GameChar
{
	protected Coordinates location;
	protected char charLetter;

	/**
	* constructor with Coordinates class
	* @param coord {Coordinates} - coordinates class with the location of the character
	* @param charLetter {char} - letter that represents the character
	*/
	protected GameChar(Coordinates coord, char charLetter)
	{
		this.location = coord;

		this.charLetter = charLetter;
	}

	/**
	* @returns a Coordinates class containing char coodinates
	*/
	protected Coordinates getCharCoordinates()
	{
		return this.location.clone();
	}

	/**
	* Returns character x coordinate
	* @return x coordinate
	*/
	protected int getX()
	{
		return location.getX();
	}

	/**
	* Returns character y coordinate
	* @return y coordinate
	*/
	protected int getY()
	{
		return location.getY();
	}

	/**
	* moves character to the right
	*/
	protected void moveRight()
	{
		this.location.addX(1);
	}

	/**
	* moves character to the left
	*/
	protected void moveLeft()
	{
		location.addX(-1);
	}

	/**
	* moves up the character
	*/
	protected void moveUp()
	{
		location.addY(-1);
	}

	/**
	* moves down the character
	*/
	protected void moveDown()
	{
		location.addY(1);
	}

	/**
	* @returns the current letter that represents the game character
	*/
	protected char characterActiveLetter()
	{
		return this.charLetter;
	}

	/**
	* changes character map representation to letter
	* @param letter {char} - letter that represents the character
	*/
	protected void setCharTo(char letter)
	{
		this.charLetter = letter;
	}

	/**
	* inserts a command to the character
	* @param command {char} - one of the following: a w s d
	*/
	protected void interpertCommand(char command)
	{
		switch(command)
		{
			case 'w':
				moveUp();
				return;
			case 's':
				moveDown();
				return;
			case 'a':
				moveLeft();
				return;
			case 'd':
				moveRight();
				return;
			default:
				return;
		}
	}

	/**
	  * determines if the character catches the other character with coordinates c
	  * @param c {Coordinates} - coordinates to catch
	  * @return true if this character catches the other with coordinates c
	  */
	public boolean gotchYa(Coordinates c)
	{
		if(location.isVerticalOrHorizontalNeighbor(c))
			return true;
		else
			return false;
	}
}
