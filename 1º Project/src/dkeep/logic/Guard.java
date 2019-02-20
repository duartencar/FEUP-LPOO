package dkeep.logic;

/**
 * Represents the guard character with different characteristics
 * @see GameChar
 */
public abstract class Guard extends GameChar
{
	protected char[] routine;
	protected int routineIndex = 0;
	protected boolean movingFoward = true;
	protected boolean isSleeping = false;

	/**
	  * Guards contructor
	  * @param coord {Coordinates} - guard coordinates
	  * @param letter {char} - representation of the guard in game matrix
	  * @param routine {char[]} - routine of the guard
	  */
	public Guard(Coordinates coord, char letter, char[] routine)
	{
		super(coord, letter);

		this.routine = routine;
	}

	/**
	  * Another guard's constructor 
	  * @param coord {Coordinates} - coordinates of guard
	  * @param letter {char} - guards letter
	  */
	public Guard(Coordinates coord, char letter)
	{
		super(coord, letter);

		this.routine = null;
	}

	/**
	  * checks if this guard has a routine or if he's static
	  * @return true if he has a routine, false otherwise
	  */
	public boolean hasRoutine()
	{
		if(this.routine!= null)
			return true;
		else
			return false;
	}

	/**
	  * sends and inverse routine movement of the guard(given in the param), usually when they need to invert their shift this function is called
	  * @param command {char} - command to invert
	  * @return inverted routine command (char)
	  */
	public char getInverseCommand(char command)
	{
		switch(command)
		{
			case 'a':
				return 'd';
			case 's':
				return 'w';
			case 'd':
				return 'a';
			case 'w':
				return 's';
			default:
				return 'E';
		}
	}

	/**
	  * perform the guard routine
	  */
	public void performRoutine()
	{
		char command;

		if(movingFoward)
		{
			command = routine[routineIndex];

			addIndex(1);
		}
		else
		{
			command = getInverseCommand(routine[routineIndex]);

			addIndex(-1);
		}

		interpertCommand(command);
	}

	/**
	  * See if guard catches the player
	  * @param c {Coordinates} - coordinates to catch
	  * @return true if guard catches, false otherwise
	  */
	@Override
	public boolean gotchYa(Coordinates c)
	{
		if(isSleeping)
			return false;
		else if(location.equals(c))
			return true;
		else if(Math.abs(getX() - c.getX()) == 1 && Math.abs(getY() - c.getY()) == 0) //if they are side by side on the same line
			return true;
		else if(Math.abs(getX() - c.getX()) == 0 && Math.abs(getY() - c.getY()) == 1) //if they are side by side on the same column
			return true;
		else
			return false;
	}

	/**
	  * increase or decrement the routine's index
	  * @param x {int} - number to add
	  */
	public void addIndex(int x)
	{
		routineIndex += x;

		if(routineIndex < 0)
			routineIndex = routine.length - 1;

		if(routineIndex == routine.length)
			routineIndex = 0;
	}

	/**
	  * checks if guard is sleeping
	  * @return true if he is indeed sleeping
	  */
	public boolean isGuardSleeping()
	{
		return this.isSleeping;
	}
	
	/**
	  * checks if guard is moving in the normal direction
	  * @return true if is moving right or false if he is moving in the inverted way
	  */
	public boolean isGuardMovingFoward()
	{
		return this.movingFoward;
	}
}
