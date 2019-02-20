package dkeep.logic;

/**
 * Represents a normal ogre inherited from the ArmedOgre Class
 * @see ArmedOgre
 */
public class Ogre extends ArmedOgre
{
	/**
	  * Ogre constructor
	  * @param coordiantes {Coordinates} - ogre coordinates
	  * @param letter {char} - ogre letter in game matrix
	  */
	public Ogre(Coordinates coordinates, char letter)
	{
		super(coordinates, letter);

		setClubRepresentation(' ');
	}

	/**
	  * override gotchYa function of ArmedOgre, checks if player is caught
	  * @param c {Coordinates} - coordinates of player
	  * @return true player is caught
	  */
	@Override
	public boolean gotchYa(Coordinates c)
	{
		if(isOgreStunned())
			return false;
		if(location.isVerticalOrHorizontalNeighbor(c))
			return true;
		else
			return false;
	}

	/**
	  * this ogre is static so this override function only gives a default char
	  * @return default char 'E'
	  */
	@Override
	public char getRandomCommand()
	{
		return 'E';
	}
}
