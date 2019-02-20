package dkeep.logic;

/**
 * Type of guard(inhereted from Guard Class), represents the rookie guard, the usual normal guard
 * @see Guard
 */
public class Rookie extends Guard
{
	/**
	  * Rookie constructor
	  * @param coord {Coordinates} - guard location
	  * @param letter {char} - char representation of the guard
	  */
	public Rookie(Coordinates coord, char letter)
	{
		super(coord, letter);
	}
	
	/**
	  * Other Rookie constructor
	  * @param coordinates {Coordinates} - guard location
	  * @param letter {char} - char representation of the guard
	  * @param routine {char[]} - guard routine
	  */
	public Rookie(Coordinates coord, char letter, char[] routine)
	{
		super(coord, letter, routine);
	}
}
