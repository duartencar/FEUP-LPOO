package dkeep.logic;

import java.util.ArrayList;

/**
 * Controls the game levels
 * @see Level
 */
public class LevelController
{

	private int levelIndex;
	private ArrayList<Level> levels;
	private Level current;
	
	/**
	  * Default constructor
	  */
	public LevelController()
	{
		levelIndex = 0;
		
		levels = new ArrayList<Level>();
	}
	
	/**
	  * checks if the current level is the last
	  * @return true if this is the last level
	  */
	public boolean isItLastLevel()
	{
		return (levelIndex + 1) == numberOfLevels();
	}
	
	/**
	  * adds a new level to the game
	  * @param x {Level} - level object to be added
	  */
	public void addLevel(Level x)
	{
		if(x == null || !(x instanceof Level))
			throw new IllegalArgumentException();
		
		levels.add(x);
	}
	
	/**
	  * goes to the next game level
	  * @return true if it is successful or false if the last level was the ultimate and there is no level left
	  */
	public boolean goToNextLevel()
	{
		if(isItLastLevel())
			return false;
		
		levelIndex++;
			
		current = levels.get(levelIndex);
			
		return true;
	}
	
	/**
	  * gets a current level object clone
	  * @return the cloned Level object
	  */
	public Level getCurrentLevel()
	{
		return current.clone();
	}
	
	/**
	  * gets the current level index
	  * @return the number of the current level
	  */
	public int getCurrentLevelIndex()
	{
		return levelIndex;
	}
	
	/**
	  * @return number of levels in the game
	  */
	public int numberOfLevels()
	{
		return levels.size();
	}
	
	/**
	  * tries to initialize the game
	  * @return true if there's at least 1 level to play, false otherwise
	  */
	public boolean initialize()
	{
		if(numberOfLevels() == 0)
			return false;
	  
		current = levels.get(0).clone();
		
		return true;
	}
}
