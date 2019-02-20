package dkeep.logic;

/**
 * Represents the player inhereted from the GameChar class
 * @see GameChar
 */
public class Player extends GameChar
{
	private boolean playerHasPickedKey;
	
	private boolean playerHasClub = false;

	/**
	  * Player constructor
	  * @param coords {Coordinates} - player coordinates
	  * @param letter {char} - player representation in game matrix
	  */
	public Player(Coordinates coords, char letter)
	{
		super(coords, letter);

		playerHasPickedKey = false;
	}

	/**
	  * When player picks the game keys. Changes his representation(char) to'K'
	  */
	public void playerPicksKey()
	{
		playerHasPickedKey = true;

		setCharTo('K');
	}

	/**
	  * check if player has already picked the game keys 
	  * @return true if the player picked the keys, false otherwise
	  */
	public boolean hasPlayerPickedTheKey()
	{
		return playerHasPickedKey;
	}

	/**
	  * gets the player arms
	  */
	public void playerIsArmed()
	{
		playerHasClub = true;

		setCharTo('A');
	}

	/**
	  * checks if player is armed
	  * @return true if player is indeed armed, false otherwise
	  */
	public boolean isPlayerArmed()
	{
		return playerHasClub;
	}
}
