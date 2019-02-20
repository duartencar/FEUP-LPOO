package dkeep.logic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**  
 * GameLogic.java - a class that interprets player commands and verifies game characters movement
 * @see LevelController
 * @see GameMatrix
 * @see GameChar
 */ 
public class GameLogic
{
	private LevelController gameProgress;

	private boolean playerNeedsExtraStep = false;

	private GameMatrix matrix;

	private Coordinates keyLocation;

	private Player thePlayer;

	private Guard[] Guards;

	private ArmedOgre[] Ogres;

	private char finishType = '*';

	private boolean gameIsUp = false;

	private String gameMessage = "";

	/**  
	 * Creates the necessary objects to initialize a game.
	 * @return A GameLogic object.  
	 */  
	public GameLogic()
	{
		this.gameIsUp = false;

		gameProgress = new LevelController();
	}

	/**  
	 * Creates the necessary objects to initialize a game.
	 * @param levelToPlay - Level object to initialize the game.
	 * @return A initialized and ready to interact GameLogic object.  
	 */  
	public GameLogic(Level levelToPlay)
	{
		gameProgress = new LevelController();

		gameProgress.addLevel(levelToPlay);

		if(gameProgress.initialize())
		{
			updateGameLogicLevel();

			this.gameIsUp = true;
		}
	}

	/**  
	 * Changes game message.  
	 * @param msg - String message to be inserted.
	 */  
	public void setGameMessage(String msg)
	{
		gameMessage = msg;
	}

	/**  
	 * Gets the last inserted game message.  
	 * @return Game message.
	 */  
	public String getGameMessage()
	{
		return gameMessage;
	}

	/**  
	 * Signalizes the game that player needs to make an extra step to leave the current level.
	 */  
	private void setExtraSteptToPlayer()
	{
		playerNeedsExtraStep = true;
	}

	/**  
	 * @return If player needs an extra step to leave the dungeon.
	 */  
	public boolean doesPlayerNeedAnotherStep()
	{
		return playerNeedsExtraStep;
	}

	/**  
	 * Updates every game element position and checks if player has won, lost or if it keeps going.
	 */  
	public void updateGameCharPositions()
	{
		checkPlayerProgression();

		checkIfPlayerReachedKey();

		updateGuardsPositions();

		updateOgresPosition();

		checkIfPlayerIsCaptured();
	}

	/**  
	 * @return A character representing the type of game ending.
	 */  
	public char getGameEndStatus()
	{
		return this.finishType;
	}

	/**  
	 * Goes through all the level guards and updates their position if necessary.
	 */  
	public void updateGuardsPositions()
	{
		for(Guard character: Guards)
		{		
			if(character.hasRoutine())
				character.performRoutine();
		}
	}

	/**  
	 * Goes through all the level ogres and updates their position if necessary.
	 */  
	public void updateOgresPosition()
	{
		Coordinates charCoords;

		Coordinates clubCoords;

		Character[] commands = {'a', 'w', 's', 'd'};

		Random x = new Random(System.currentTimeMillis());

		int commandIndex;

		List<Character> possibleCommands = new ArrayList<Character>();

		char command;

		for(ArmedOgre ogre: Ogres)
		{
			charCoords = ogre.getCharCoordinates();

			clubCoords = ogre.getClubCordinates();

			command  = ogre.getRandomCommand();

			if(ogre.isOgreStunned())
				ogre.reduceStunnedRounds();

			else if(isCommandExecutable(ogre, command) && ogre.hasOgreMoved())
			{
				ogre.interpertCommand(command);

				charCoords = ogre.getCharCoordinates();

				ogre.setClubLocation(charCoords.clone());

				if(keyLocation.equals(charCoords))
					ogre.setCharTo('$');
				else
					ogre.setCharTo('O');

				for(int j = 0; j < 4; j++)
					if(isCommandExecutable(ogre, commands[j]))
						possibleCommands.add(commands[j]);

				if(possibleCommands.size() != 0)
				{
					commandIndex = x.nextInt(possibleCommands.size());

					command = possibleCommands.get(commandIndex);

					ogre.giveCommandToClub(command);

					//gets new coordinates
					clubCoords = ogre.getClubCordinates();

					if(keyLocation.equals(clubCoords))
						ogre.setClubRepresentation('$');
					else
						ogre.setClubRepresentation('*');
				}

				//clears list of commands
				possibleCommands.clear();
			}
		}
	}

	/**
	 * Fills a 2D array with the level map and level elements and returns it.
	 * @return a map with the game characters in position.
	 */
	public char[][] getGameMatrix()
	{
		char [][] map = this.matrix.getMap();

		addPlayerToMap(map);

		addGuardsToMap(map);

		addOgresToMap(map);

		return map;
	}

	/**
	 * Fills a map position corresponding to player coordinates with the current player representation.
	 * @param map - 2D char array with map representation.
	 */
	private void addPlayerToMap(char[][] map)
	{
		map[thePlayer.getY()][thePlayer.getX()] = thePlayer.characterActiveLetter();
	}

	/**
	 * @return a ready to print string with map representation.
	 */
	public String getGameMap()
	{
		char[][] map = getGameMatrix();

		return matrix.getReadyToPrintMap(map);
	}

	/**
	 * Prints the current game matrix.
	 * @see GameMatrix.printForeignMatrix
	 */
	public void printGameMatrix()
	{
		matrix.printForeignMatrix(getGameMatrix());
	}

	/**
	 * Goes through all ogres and checks if the player stuns some of them.
	 */
	public void checkIfThereAreStunnedOgres()
	{
		Coordinates ogreCoordinates;

		if(!thePlayer.isPlayerArmed())
			return;

		for(ArmedOgre ogre: Ogres)
		{
			ogreCoordinates = ogre.getCharCoordinates();

			if(thePlayer.gotchYa(ogreCoordinates) && !ogre.isOgreStunned())
				ogre.stun();
		}
	}

	/**
	 * Inserts guards in the map according to their location
	 */
	public void checkIfPlayerReachedKey()
	{
		if(this.keyLocation.equals(thePlayer.getCharCoordinates()))
		{					
			if(!doesPlayerNeedAnotherStep())
			{
				matrix.playerHasReachedTheKey();

				setGameMessage("You pulled the lever!");
			}

			thePlayer.playerPicksKey();

			if(getGameMessage() != "You pulled the lever!")
				setGameMessage("You got the key!");
		}
	}

	/**
	 * Inserts guards in the map according to their location
	 * @param map {char[][]} - bi-dimensional char array representing the game map
	 */
	public void addGuardsToMap(char[][] map)
	{
		Coordinates guardCoordinates;

		for(int i = 0; i < this.Guards.length; i++)
		{		
			//get the guard coordinates
			guardCoordinates = this.Guards[i].getCharCoordinates();

			map[guardCoordinates.getY()][guardCoordinates.getX()] = this.Guards[i].characterActiveLetter();
		}
	}

	/**
	 * Inserts Ogres in the map according to their location.
	 * @param map - 2D char array representing the game map.
	 */
	public void addOgresToMap(char[][] map)
	{
		Coordinates charCoords;

		Coordinates clubCoords;

		for(int i = 0; i < this.Ogres.length; i++)
		{
			charCoords = this.Ogres[i].getCharCoordinates();

			clubCoords = this.Ogres[i].getClubCordinates();
			//draws Ogre
			map[charCoords.getY()][charCoords.getX()] = this.Ogres[i].characterActiveLetter();

			map[clubCoords.getY()][clubCoords.getX()] = this.Ogres[i].getClubActiveChar();
		}	
	}

	/**
	 * When player gets to some stairs, it checks if players has won the game or simply goes to another level.
	 * @see LevelControler
	 */
	public void checkPlayerProgression()
	{
		if(checkIfPlayerHasReachedStairs())
		{
			if(gameProgress.isItLastLevel())
			{
				terminateGame('V');
			}
			else if(gameProgress.goToNextLevel())
			{
				updateGameLogicLevel();

				setGameMessage("You went to a new level!");
			}		
		}	
	}

	/**
	 * Goes to the next level.
	 * @see LevelControler
	 */
	public void updateGameLogicLevel()
	{
		goToLevel(gameProgress.getCurrentLevel());
	}

	/**
	 * Creates a guard of a given type at a given coordinate.
	 * @param typeOFGuard - type of guard to be created.
	 * @param xy - coordinates for the created guard.
	 * @return a new guard.
	 * @see Guard
	 */
	public Guard getGuardForLevel(char typeOFGuard, Coordinates xy)
	{
		Guard dungeonGuard = new Rookie(xy.clone(), 'G');

		char [] guardRoutine = {
				'a', 's', 's', 's',
				's', 'a', 'a', 'a',
				'a', 'a', 'a', 's',
				'd', 'd', 'd', 'd',
				'd', 'd', 'd', 'w',
				'w', 'w', 'w', 'w'};

		switch(typeOFGuard)
		{
		case 'R':
			dungeonGuard = new Rookie(xy.clone(), 'G', guardRoutine);
			break;
		case 'D':
			dungeonGuard = new Drunken(xy.clone(), 'G', guardRoutine);
			break;
		case 'S':
			dungeonGuard = new Suspicious(xy.clone(), 'G', guardRoutine);
			break;
		default:
			break;
		}

		return dungeonGuard;
	}

	/**
	 * Creates two default levels, with the given type of guard and number of ogres, and inserts the to game level controller.
	 * @param typeOFGuard - type of guard to be created.
	 * @param numberOfPgres - number of ogres to level.
	 * @see Level
	 * @see ArmedOgre
	 * @see Guard
	 * @see GameController
	 */
	public void populateLevels(char typeOFGuard, int numberOfPgres)
	{
		Random numGen = new Random();

		ArmedOgre ogre;

		String[] dungeonMap = {
				"XXXXXXXXXX",
				"XH  I X  X",
				"XXX XXX  X",
				"X I I X  X",
				"XXX XXX  X",
				"I        X",
				"I        X",
				"XXX XXXX X",
				"X I I Xk X",
		"XXXXXXXXXX"};

		Guard dungeonGuard = getGuardForLevel(typeOFGuard, new Coordinates(8,1));

		Level dungeonLevel = new Level(dungeonMap, 1);

		dungeonLevel.addGuard(dungeonGuard);

		//second map representation
		String[] keepMap = {
				"XXXXXXXXXX",
				"I       kX",
				"X        X",
				"X        X",
				"X        X",
				"X        X",
				"X        X",
				"X        X",
				"XA       X",
		"XXXXXXXXXX"};

		//creates second level object
		Level keepLevel = new Level(keepMap, 2);

		for(int i = 0; i < numberOfPgres; i++)
		{
			ogre = new ArmedOgre(new Coordinates(numGen.nextInt(5) + 1, numGen.nextInt(5) + 1), 'O');

			keepLevel.addArmedOgre(ogre.clone());
		}

		//sets the extra step flag to true
		keepLevel.setExtraStep();

		keepLevel.playerHasClub();

		gameProgress.addLevel(dungeonLevel);

		gameProgress.addLevel(keepLevel);

		if(gameProgress.initialize())	
			gameIsUp = true;
	}

	/**
	 * Creates two hard codded default levels and inserts the to game level controller.
	 * @see Level
	 * @see ArmedOgre
	 * @see Guard
	 * @see GameController
	 */
	public void populateLevels()
	{
		//first map representation
		String[] map1 = {
				"XXXXXXXXXX",
				"XH  I X  X",
				"XXX XXX  X",
				"X I I X  X",
				"XXX XXX  X",
				"I        X",
				"I        X",
				"XXX XXXX X",
				"X I I Xk X",
		"XXXXXXXXXX"};

		//creates first level object
		Level firstLevel = new Level(map1, 1);

		//creates a guard with the created routine
		Guard firstLevelGuard = getGuardForLevel('R', new Coordinates(8,1));

		//adds the created guard to level
		firstLevel.addGuard(firstLevelGuard);

		String[] map2 = {
				"XXXXXXXXXX",
				"I       kX",
				"X        X",
				"X        X",
				"X        X",
				"X        X",
				"X        X",
				"X        X",
				"XH       X",
		    "XXXXXXXXXX"};

		Level secondLevel = new Level(map2, 2);

		ArmedOgre secondLevelOgre = new ArmedOgre(new Coordinates(4, 1), 'O');

		secondLevel.addArmedOgre(secondLevelOgre);

		secondLevel.setExtraStep();

		secondLevel.playerHasClub();

		gameProgress.addLevel(firstLevel);

		gameProgress.addLevel(secondLevel);

		if(gameProgress.initialize())	
			gameIsUp = true;
	}

	/**
	 * Loads the first level.
	 * @return true if it has level to load, false if it doesn't.
	 * @see LevelController
	 */
	public boolean initializeGameController()
	{
		if(gameProgress.initialize())
			return true;

		return false;
	}

	/**
	 * @return the current level that the player is playing.
	 * @see LevelController
	 */
	public int getCurrentLevel()
	{
		return gameProgress.getCurrentLevelIndex();
	}

	/**
	 * Adds a level to the LevelController.
	 * @param levelToAdd - level to be added.
	 * @see LevelController
	 */
	public void addLevel(Level levelToAdd)
	{
		gameProgress.addLevel(levelToAdd);
	}

	/**
	 * Loads given level to LevelController
	 * @param levelToLoad - level to be loaded
	 * @see LevelController
	 */
	public void goToLevel(Level levelToLoad)
	{
		if(levelToLoad == null || !(levelToLoad instanceof Level))
			throw new IllegalArgumentException();

		gameIsUp = true;

		matrix = levelToLoad.getLevelMap();

		keyLocation = levelToLoad.getKeyLocation();

		thePlayer = new Player(levelToLoad.getPlayerStartingPoint(), 'H');

		if(levelToLoad.canPlayerFight())
			thePlayer.playerIsArmed();

		if(levelToLoad.getExtraStep())
			setExtraSteptToPlayer();

		Guards = levelToLoad.getLevelGuards();

		Ogres = levelToLoad.getLevelOgres();
	}

	/**
	 * @return True if game created successfully
	 */
	public boolean isGameOn()
	{
		return gameIsUp;
	}

	/**
	 * Stops game running and sets game finishing type to given type.
	 * @param finishType - type of game ending
	 */
	public void terminateGame(char finishType)
	{
		this.finishType = finishType;

		gameIsUp = false;
	}

	/**
	 * Checks if the command parameter is one of the possible commands
	 * @param command {char} - command to be tested
	 * returns true if command is valid
	 */
	private boolean isValidCommand(char command)
	{
		final char[] possibleCommands = {'a', 'w', 's', 'd', 'A', 'W', 'S', 'D'};

		for(int i = 0; i < possibleCommands.length; i++)
			if(command == possibleCommands[i])
				return true;

		return false;
	}

	/**
	 * Inserts given command to player object, updates game message and other characters position.
	 * @param commandToBeExecuted - direction to move player
	 */
	public void insertCommand(char commandToBeExecuted)
	{
		setGameMessage("");

		if(isCommandExecutable(getPlayer(), commandToBeExecuted))
			insertCommandToPlayer(commandToBeExecuted);
		else if(getGameMessage() != "You unlocked the door!")
			setGameMessage("Invalid move!");

		this.updateGameCharPositions();
	}

	/**
	 * Checks if the position the player is trying to move is accessible.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @return true if it does, false if it doesnt.
	 */
	private boolean isMovePossible(int x, int y, boolean isPlayer)
	{
		char element = matrix.getElementAtPosition(x, y);

		if(element == 'X' || element == 'E')
			return false;
		else if(element == 'I' && thePlayer.hasPlayerPickedTheKey() && isPlayer)
		{
			matrix.playerHasReachedTheKey();

			setGameMessage("You unlocked the door!");

			return false;
		}
		else if(element == 'I')
			return false;
		else if(element == 'S')
			return true;
		else
			return true;
	}

	/**
	 * Given a command and a coordinate of a character, check if command is executable.
	 * @param charCoordinates - character coordinates.
	 * @param command - command to be executed.
	 * @return true if command is executable else false.
	 */
	public boolean isCommandExecutable(GameChar character, char command)
	{
		if(isValidCommand(command))
		{
			int x = character.getX();
			int y = character.getY();
			boolean isPlayerCharacter = character instanceof Player;

			switch(command)
			{
			case 'a':
			case 'A':
				return isMovePossible(x - 1, y, isPlayerCharacter); //check element on the left
			case 's':
			case 'S':
				return isMovePossible(x, y + 1, isPlayerCharacter);//check element the back element
			case 'd':
			case 'D':
				return isMovePossible(x + 1, y, isPlayerCharacter); //check element to the right
			case 'w':
			case 'W':
				return isMovePossible(x, y - 1, isPlayerCharacter); //check element the front element
			default:
				return false;
			}
		}

		return false;
	}

	/**
	 * Goes through guards and ogres to check if they finally caught the player
	 */
	public void checkIfPlayerIsCaptured()
	{
		Coordinates playerCoordinates = thePlayer.getCharCoordinates();

		checkIfThereAreStunnedOgres();

		for(Guard g: Guards)
		{
			if(g.gotchYa(playerCoordinates))
			{
				terminateGame('G');
			}
		}

		for(ArmedOgre ogre: Ogres)
		{		
			if(ogre.gotchYa(playerCoordinates) && !ogre.isOgreStunned())
			{
				terminateGame('O');
			}
		}
	}

	/**
	 * Checks if player is at an edge of the map.
	 * @param playerCoordinates - player current coordinates
	 * @return True if player is at the edge of the map, false otherwise.
	 */
	private boolean isPlayerInOneOfMapEdges(Coordinates playerCoordinates)
	{
		int x = this.thePlayer.getX();
		int y = this.thePlayer.getY();

		if(x == 0 || y == 0 || x == matrix.getSideLength() - 1 || y == matrix.getSideLength() - 1)
			return true;
		else
			return false;
	}

	/**
	 * Checks if player is at the stairs or at a winning point.
	 * @return true if it does.
	 */
	public boolean checkIfPlayerHasReachedStairs()
	{
		Coordinates playerCoords = this.thePlayer.getCharCoordinates();

		if(isPlayerInOneOfMapEdges(playerCoords))
		{
			if(matrix.getElementAtPosition(playerCoords.getX(), playerCoords.getY()) == 'S' && thePlayer.hasPlayerPickedTheKey())
				return true;
			else
				return false;
		}

		return false;
	}

	/**
	 * @return Player current player coordinates.
	 */
	public Coordinates getPlayerCoordinates()
	{
		return thePlayer.getCharCoordinates();
	}

	/**
	 * @return true if player is armed and can stun the ogres.
	 */
	public boolean canPlayerFight()
	{
		return thePlayer.isPlayerArmed();
	}

	/**
	 * @return The current char that represent the player in the game map.
	 */
	public char getPlayerActiveRepresentationChar()
	{
		return thePlayer.characterActiveLetter();
	}

	/**
	 * inserts command to the player, so he moves
	 * @param command - command to be executed
	 * @see GameChar
	 */
	public void insertCommandToPlayer(char command)
	{
		thePlayer.interpertCommand(command);
	}

	/**
	 * @return The player object.
	 */
	public GameChar getPlayer()
	{
		return thePlayer;
	}

	/**
	 * @return The char representation of a given position.
	 * @param x - x coordinate.
	 * @param y - y coordinate.
	 */
	public char getElementFromGameMatrix(int x, int y)
	{
		return matrix.getElementAtPosition(x, y);
	}
}
