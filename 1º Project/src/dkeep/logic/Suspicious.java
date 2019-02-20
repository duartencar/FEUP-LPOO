package dkeep.logic;
import java.util.Random;

/**
 * Type of guard(inhereted from Guard Class), represents the suspicious guard which turns around randomly to check if he heard something
 * @see Guard
 */
public class Suspicious extends Guard
{
	/**
	  * Suspicious constructor
	  * @param coordinates {Coordinates} - guard location
	  * @param letter {char} - char representation of the guard
	  * @param routine {char[]} - guard routine
	  */
	public Suspicious(Coordinates coordinates, char letter, char[] routine)
	{
		super(coordinates, letter, routine);
	}

	/**
	  * Specific Suspicious guard routine
	  */
	@Override
	public void performRoutine()
	{
		if(isGuardMovingFoward())
		{
			interpertCommand(routine[routineIndex]);

			addIndex(1);
		}
		else
		{
			addIndex(-1);

			interpertCommand(getInverseCommand(routine[routineIndex]));
		}

		if(getIfGuardHeardSomething())
		{
			if(isGuardMovingFoward())
				movingFoward = false;
			else
				movingFoward = true;
		}
	}

	/**
	  * Checks if guard got suspicious and turned his shift around
	  * @return true if guard turned his shift around
	  */
	public boolean getIfGuardHeardSomething()
	{
		Random z = new Random();

		int i = z.nextInt(100);

		return (i >= 79);
	}
}
