package dkeep.logic;
import java.util.Random;

/**
 * Inhereted class from GameChar, representing the armed ogre character.
 * @see GameChar
 */
public class ArmedOgre extends GameChar
{
	private boolean hasntMoved;
	private GameChar club;
	private int stunnedRound = -1;

	/**
	* constructor of class ArmedOgre
	* @param coordinates {Coordinates} - coordinates of ogre
	* @param letter {char} - character that defines the ogre in game matrix
	*/
	public ArmedOgre(Coordinates coordinates, char letter)
	{
		super(coordinates, letter);

		club = new GameChar(coordinates.clone(), '*');

		giveCommandToClub('s');

		hasntMoved = true;
	}

	/**
	* Gives a char{a,s,d,w} which represents a random command to make the ogre move randomly
	* @return random command(char) to be interpreted 
	*/
	public char getRandomCommand()
	{
		char command = 'E';
		
		if(hasntMoved)
			hasntMoved = false;
		else
		{
			final char[] movements = {'a', 's', 'd', 'w'};

			Random x = new Random();

			int randomMovement = x.nextInt(movements.length);

			command = movements[randomMovement];
		}

		return command;
	}

	/**
	* Returns a boolean which tells if the ogre has already moved in the game
	* @return true if ogre has already moved once in game 
	*/
	public boolean hasOgreMoved()
	{
		return !hasntMoved;
	}

	/**
	* Overriding the Gamechar(father class) gotchYa function, which determines if the player was caught by the ogre
	* @param c {Coordinates} - player coordinates
	* @return true if player caught
	*/
	@Override
	public boolean gotchYa(Coordinates c)
	{
		if(!isOgreStunned() && (location.isVerticalOrHorizontalNeighbor(c) || club.location.isVerticalOrHorizontalNeighbor(c)))
			return true;
		else
			return false;
	}

	/**
	* Gives a command to the ogre club
	* @param command {char} - command to club
	*/
	public void giveCommandToClub(char command)
	{
		club.interpertCommand(command);
	}

	/**
	* gets club coordinates
	* @return Coordinates object representing the club location
	*/
	public Coordinates getClubCordinates()
	{
		return club.getCharCoordinates();
	}

	/**
	* sets the club representation(char) to the symbol given as parameter
	* @param symbol {char} - new character to represent club
	*/
	public void setClubRepresentation(char symbol)
	{
		club.setCharTo(symbol);
	}

	/**
	* gets the club char representation in the matrix
	* @return letter that represents the club in the game matrix
	*/
	public char getClubActiveChar()
	{
		return club.characterActiveLetter();
	}

	/**
	* changes the club coordinates
	* @param coordinates {Coordinates} - new club coordinates
	*/
	public void setClubLocation(Coordinates coordinates)
	{
		club.location = coordinates;
	}

	/**
	* determines if the ogre is currently stunned
	* @return true if ogre is currently stunned
	*/
	public boolean isOgreStunned()
	{
		return (stunnedRound > 0);
	}

	/**
	* ArmedOgre toString function. Returns a string with the ogre location.
	* @return string with coordenates of ogre
	*/
	public String toString()
	{
		return "" + this.location;
	}

	/**
	* stuns the ogre for 2 rounds changing his and the club representations in the game
	*/
	public void stun()
	{
		setCharTo('8');

		setClubRepresentation(' ');

		stunnedRound = 2;
	}

	/**
	* unstuns the ogre
	*/
	public void unStun()
	{
		setCharTo('O');

		setClubRepresentation('*');
	}

	/**
	* decrements by 1 the stunnedRound parameter of the ogre, possibly unstunning him
	*/
	public void reduceStunnedRounds()
	{
		stunnedRound--;

		if(stunnedRound == 0)
			unStun();
	}

	/**
	* gets ogre representation in the game(char)
	* @return char that represents the ogre in the game matrix
	*/
	public char getActiveChar()
	{
		return characterActiveLetter();
	}

	/**
	* Overrides fathers clone funtion, giving a clone of the current ogre
	* @return a ArmedOgre object cloned
	*/
	@Override
	protected ArmedOgre clone()
	{
		ArmedOgre newOgre = new ArmedOgre(location.clone(), 'O');
		
		return newOgre;
	}
}
