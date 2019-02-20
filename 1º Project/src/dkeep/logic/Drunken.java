package dkeep.logic;

import java.util.Random;

/**
 * Type of guard(inhereted from Guard Class) representing the drunken guard. Falls asleep in his shift from time to time.
 * @see Guard 
 */
public class Drunken extends Guard
{
	/**
	  * Drunken's constructor function
	  * @param coordinates {Coordinates} - guard location
	  * @param letter {char} - letter that represents the drunken guard
	  * @param routine {char[]} - guards routine
	  */
	public Drunken(Coordinates coordinates, char letter, char[] routine)
	{
		super(coordinates, letter, routine);
	}

	/**
	  * Puts the guard to sleep(changing representation to 'g')
	  */
	public void fallAsleep()
	{
		setCharTo('g');

		isSleeping = true;

		movingFoward = !movingFoward;
	}

	/**
	  * Awakes the guard(giving him his initial representation back)
	  */
	public void wakeUp()
	{
		setCharTo('G');

		isSleeping = false;
	}

	/**
	  * determines if the guard starts to sleep(20%chance of happening)
	  * @return true if starts to sleep, false otherwise
	  */
	public boolean getIfGuardStartsToSleep()
	{
		Random z = new Random();

		int i = z.nextInt(100);

		//20 % change of falling asleep
		return (i >= 80);
	}

	/**
	  * determines if guard wakes up
	  * @return true if guard awakes
	  */
	public boolean getIfGuardAwakes()
	{
		return !getIfGuardStartsToSleep();
	}

	/**
	  * Overrides the father(Guard Class) performRoutine, which is specific of the drunken guard and performs his routine
	  */
	@Override
	public void performRoutine()
	{
		if(isGuardSleeping())
		{
			if(getIfGuardAwakes())
				wakeUp();
		}
		else if(!isGuardSleeping())
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

			if(getIfGuardStartsToSleep())
				fallAsleep();
		}
	}
}
