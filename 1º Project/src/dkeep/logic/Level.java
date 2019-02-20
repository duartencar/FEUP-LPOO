package dkeep.logic;

import java.util.ArrayList;

/**
 * Represents a Level in the game
 * @see GameMatrix
 * @see Coordinates
 * @see Guard
 * @see ArmedOgre
 */
public class Level
{
  private GameMatrix levelMap;
  private int levelIndex;
  private boolean correct;
  private boolean needsExtraStep = false;
  private boolean playerCanHaveClub;
  private Coordinates keyLocation;
  private Coordinates playerStartingCoords;
  private Guard[] levelGuards;
  private ArmedOgre[] levelOgres;

  /**
   * Level default constructor
   */
  public Level()
  {
    this.levelMap = new GameMatrix();
    this.setLevelIndex(-1);
    this.correct = false;
    this.needsExtraStep = false;
    this.playerCanHaveClub = false;
    this.keyLocation = new Coordinates(-1, -1);
    this.playerStartingCoords = new Coordinates(-1, -1);
    this.levelGuards = new Guard[0];
    this.levelOgres = new ArmedOgre[0];
  }

  /**
   * Level constructor
   * @param gameMap - gives an array with string representing the game map
   * @param levelIndex {int} - Level index in the game levels
   */
  public Level(String[] gameMap, int levelIndex)
  {
    vailidateLevelIndex(levelIndex);
    
    fillLevelMatrix(gameMap);

    keyLocation = levelMap.getCharCoordinates('k');

    ArrayList<ArrayList<Coordinates>> charPositions = levelMap.getGameChars();
    
    fillGameChars(charPositions);
    
    playerCanHaveClub = levelMap.isPlayerArmed();

    correct = (levelGuards.length > 0 || levelOgres.length > 0);
  }
  
  void fillGameChars(ArrayList<ArrayList<Coordinates>> charPositions)
  {
  	final int playerIndex = 0;
    final int guardsIndex = 1;
    final int ogresIndex = 2;
    Coordinates charPos;
    
    if(charPositions.get(playerIndex).size() != 1)
    	throw new IllegalArgumentException("Wrong number of players on game map!");
    
  	playerStartingCoords = charPositions.get(playerIndex).get(0).clone();

    levelGuards = new Guard[charPositions.get(guardsIndex).size()];

    levelOgres = new ArmedOgre[charPositions.get(ogresIndex).size()];

    for(int i = 0; i < levelGuards.length; i++)
    {
    	charPos = charPositions.get(guardsIndex).get(i).clone();

      levelGuards[i] = new Rookie(charPos.clone(), 'G');
    }

    for(int i = 0; i < levelOgres.length; i++)
    {
    	charPos = charPositions.get(ogresIndex).get(i).clone();

      levelOgres[i] = new ArmedOgre(charPos.clone(), 'O');
    }
  }
  
  void fillLevelMatrix(String[] map)
  {
  	 try
     {
     this.levelMap = new GameMatrix(map);
     }
     catch(IllegalArgumentException e)
     {
     	System.out.println(e.getMessage());
     	
     	throw new IllegalArgumentException("Invalid map for Level!");
     }
  }
  
  void vailidateLevelIndex(int levelIndex)
  {
  	if(levelIndex > 0)
      setLevelIndex(levelIndex);
    else
    	throw new IllegalArgumentException("Error: Invalid levelIndex detected -> " + levelIndex);
  }

	/**
	 * gets the game matrix
	 * @return a GameMatrix object of this level
	 */
  public GameMatrix getLevelMap()
  {
    return this.levelMap;
  }

	/**
	 * gets the key coordinates
	 * @return coordinates object of the key
	 */
  public Coordinates getKeyLocation()
  {
    return this.keyLocation;
  }

	/**
	 * gets the player starting location
	 * @return the player starting coordinates object 
	 */
  public Coordinates getPlayerStartingPoint()
  {
    return this.playerStartingCoords;
  }

	/**
	 * gets the level guards
	 * @return an array with all the level guards
	 */
  public Guard[] getLevelGuards()
  {
    return this.levelGuards;
  }

  /**
   * gets the level armed ogres
   * @return an array with all the level armed ogres
   */
  public ArmedOgre[] getLevelOgres()
  {
    return this.levelOgres;
  }

  /**
   * allows the player to have a club
   */
  public void playerHasClub()
  {
    playerCanHaveClub = true;
  }

  /**
   * gets if player can fight, i.e. if he can have a club 
   * @return true if player can fight or false otherwise
   */
  public boolean canPlayerFight()
  {
    return playerCanHaveClub;
  }

  /**
   * get if the level was created successfully
   * @return true if it was or false otherwise
   */
  public boolean levelCreatedSuccessfully()
  {
    return this.correct;
  }

  /**
   * sets the extra step boolean
   */
  public void setExtraStep()
  {
    this.needsExtraStep = true;
  }
  /**
   * gets the extra step boolean 
   * @return true if players needs to get that extra step to pass the level
   */
  public boolean getExtraStep()
  {
    return this.needsExtraStep;
  }

  /**
   * adds a guard to the game
   * @param newGuard {Guard} - new guard to add
   */
  public void addGuard(Guard newGuard)
  {
    Guard[] x = this.levelGuards;

    Guard[] newGuardsArray = new Guard[x.length + 1];

    for(int i = 0; i < x.length; i++)
      newGuardsArray[i] = x[i];

    newGuardsArray[newGuardsArray.length - 1] = newGuard;

    this.levelGuards = newGuardsArray;
  }

  /**
   * adds an armored ogre
   * @param newOgre - new ogre to add
   */
  public void addArmedOgre(ArmedOgre newOgre)
  {
    ArmedOgre[] x = this.levelOgres;

    ArmedOgre[] newOgresArray = new ArmedOgre[x.length + 1];

    for(int i = 0; i < x.length; i++)
      newOgresArray[i] = x[i];

    newOgresArray[newOgresArray.length - 1] = newOgre;

    this.levelOgres = newOgresArray;
  }

  /**
   * adds a normal ogre to the game
   * @param newOgre - ogre to add
   */
  public void addOgre(Ogre newOgre)
  {
    ArmedOgre[] x = this.levelOgres;

    ArmedOgre[] newOgresArray = new ArmedOgre[x.length + 1];

    for(int i = 0; i < x.length; i++)
      newOgresArray[i] = x[i];

    newOgresArray[newOgresArray.length - 1] = newOgre;

    this.levelOgres = newOgresArray;
  }

  /**
   * adds a normal ogre to the game
   * @param newOgre - ogre to add
   * @param letter - letter that now defines the new ogre
   */
  public void addOgre(Coordinates coords, char letter)
  {
    Ogre newOgre = new Ogre(coords, letter);

    ArmedOgre[] x = this.levelOgres;

    ArmedOgre[] newOgresArray = new ArmedOgre[x.length + 1];

    for(int i = 0; i < x.length; i++)
      newOgresArray[i] = x[i];

    newOgresArray[newOgresArray.length - 1] = newOgre;

    this.levelOgres = newOgresArray;
  }

  /**
   * clones the current level
   * @return a cloned Level object
   */
	@Override
	public Level clone()
	{
		Level clone = new Level();
		
		clone.levelMap = this.levelMap.clone();
		
		clone.keyLocation = this.keyLocation.clone();
		
		clone.levelGuards = this.levelGuards.clone();
		
		clone.levelOgres = this.levelOgres.clone();
		
		clone.playerStartingCoords = this.playerStartingCoords.clone();
		
		clone.playerCanHaveClub = canPlayerFight();
		
		clone.needsExtraStep = getExtraStep();
		
		clone.levelIndex = this.getLevelIndex();
		
		clone.correct = levelCreatedSuccessfully();
		
		return clone;
	}
	/**
	 * gets the level index
	 * @return this level index 
	 */
	public int getLevelIndex()
	{
		return levelIndex;
	}

	/**
	 * sets the level index
	 * @param levelIndex - new index for this level
	 */
	public void setLevelIndex(int levelIndex)
	{
		this.levelIndex = levelIndex;
	}

	/**
	 * @return string with the level matrix
	 */
	@Override
	public String toString()
	{
		String toReturn = "";
		
		char[][] map = levelMap.getMap();
		
		Coordinates xy = this.playerStartingCoords;
		
		if(playerCanHaveClub)	
			map[xy.getY()][xy.getX()] = 'A';
		else
			map[xy.getY()][xy.getX()] = 'H';
		
		for(ArmedOgre ogre: this.levelOgres)
			map[ogre.getY()][ogre.getX()] = 'O';
		
		for(int y = 0; y < map.length; y++)
		{
			for(int x = 0; x < map[0].length; x++)
			{
				toReturn += map[y][x];
			}
			
			toReturn += '\n';
		}
		
		return toReturn;
	}
}
