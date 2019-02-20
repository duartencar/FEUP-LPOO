package dkeep.logic;

import java.util.ArrayList;

/**  
 * GameMatrix.java - a class that holds a representation of level map.
 * @see LevelController
 * @see GameMatrix
 * @see GameChar
 */ 
public class GameMatrix
{
	private char[][] matrix;
	private int sideLength = 0;
	private boolean playerIsArmed = false;
	private boolean doorsAreOpened = false;

	/**  
	 * Creates an empty 10x10 matrix.
	 * @return A GameMatrix object.  
	 */  
	public GameMatrix()
	{
		this.matrix = new char[10][10];

		this.sideLength = 10;
	}

	/**  
	 * Creates the necessary objects to initialize a game.
	 * @param matrix - an array of strings containing the map lines. 
	 * @throws  IllegalArgumentException if given matrix is not a square or if it has invalid game characters.
	 */  
	public GameMatrix(String[] matrix) throws IllegalArgumentException
	{
		if(checkIfSquareMatrix(matrix))
		{
			this.matrix = new char[matrix.length][matrix.length];

			if(!fillMatrix(matrix))
				throw new IllegalArgumentException("Invalid char detected!");

			this.setSideLength(matrix.length);
		}
		else
			throw new IllegalArgumentException("The number of rows is different from the number of columns!");
	}
	
	/**  
	 * Fills a the map representation with given map.
	 * @param matrix - an array of strings containing the map lines. 
	 * @return true if matrix was filled correctly.
	 */  
	private boolean fillMatrix(String[] map)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[i].length; j++)
			{
				if(this.checkIfValidChar(map[i].charAt(j)))
					this.matrix[i][j] = map[i].charAt(j);
				else
					return false;
			}
		}
		return true;
	}

	/**   
	 * @return 2D char array copy of the stored map.
	 */  
	public char [][] getMap()
	{
		char[][] map = new char[this.sideLength][this.sideLength];

		for(int y = 0; y < this.sideLength; y++)
			for(int x = 0; x < this.sideLength; x++)
				map[y][x] = this.matrix[y][x];

		return map;
	}
	
	/**   
	 * Prints to console a given map.
	 * @param map - 2D char representing a map.
	 */ 
	public void printForeignMatrix(char[][] map)
	{
		for(int y = 0; y < map.length; y++)
		{
			for(int x = 0; x < map.length; x++)
				System.out.print(map[y][x] + " ");

			System.out.print('\n');
		}
	}
	
	/**   
	 * @param map - 2D char representing a map.
	 * @return a string representing the map, prepared to be printed to console.
	 */ 
	public String getReadyToPrintMap(char[][] map)
	{
		String stringMap = "";
		
		for(char[] line: map)
		{
			for(char c : line)
				stringMap += c + " ";	  
			
			stringMap += "\n";
		}
		
		return stringMap;
	}
	
	/**   
	 * @return if player is armed.
	 */ 
	public boolean isPlayerArmed()
	{
		return playerIsArmed;
	}
	
	/**
	 * @param heroCoordinates - array with hero coordinates.
	 * @param guardsCoordinates - array with guards coordinates.
	 * @param ogresCoordinates - array with ogres coordinates.
	 * @return Array of coordinates array containing all characters coordinates.
	 */ 
	private ArrayList<ArrayList<Coordinates>> getToReturn(ArrayList<Coordinates> heroCoordinates, ArrayList<Coordinates> guardsCoordinates, ArrayList<Coordinates> ogresCoordinates)
	{
		ArrayList<ArrayList<Coordinates>> toReturn = new ArrayList<ArrayList<Coordinates>>();

		toReturn.add(heroCoordinates);
		
		toReturn.add(guardsCoordinates);
		
		toReturn.add(ogresCoordinates);
		
		return toReturn;
	}
	
	/**
	 * Fills the given arrays with respective char coordinates.
	 * @param heroCoordinates - array where hero coordinates should be stored.
	 * @param guardsCoordinates - array where guards coordinates should be stored.
	 * @param ogresCoordinates - array where ogres coordinates should be stored.
	 */ 
	private void fillWithCharCoords(ArrayList<Coordinates> heroCoordinates, ArrayList<Coordinates> guardsCoordinates, ArrayList<Coordinates> ogresCoordinates)
	{
		for(int i = 0; i < this.matrix.length; i++)
		{
			for(int j = 0; j < this.matrix[i].length; j++)
			{
				switch(this.matrix[i][j])
				{
					case 'A':
						playerIsArmed = true;
					case 'H':
						heroCoordinates.add(new Coordinates(j, i));
						setCharacterToPostion(j, i, ' ');
						break;
					case 'G':
						guardsCoordinates.add(new Coordinates(j, i));
						setCharacterToPostion(j, i, ' ');
						break;
					case 'O':
						ogresCoordinates.add(new Coordinates(j, i));
						setCharacterToPostion(j, i, ' ');
						break;
					default:
						break;
				}
			}
		}
	}

	/**
	* Detects game characters in stored matrix.
	* @return Array of coordinates array with detected game characters.
	*/
	public ArrayList<ArrayList<Coordinates>> getGameChars()
	{
		ArrayList<Coordinates> heroCoordinates = new ArrayList<Coordinates>();
		
		ArrayList<Coordinates> guardsCoordinates = new ArrayList<Coordinates>();
		
		ArrayList<Coordinates> ogresCoordinates = new ArrayList<Coordinates>();

		fillWithCharCoords(heroCoordinates, guardsCoordinates, ogresCoordinates);

		return getToReturn(heroCoordinates, guardsCoordinates, ogresCoordinates);
	}

	/**
	* Checks if matrix is square by comparing the number of cols and rows
	* @param matrix - map of characters
	* @return true if matrix is square.
	*/
	public boolean checkIfSquareMatrix(String[] matrix)
	{
		int length = matrix.length;

		for(int i = 0; i < matrix.length; i++)
			if(length != matrix[i].length())
				return false;

		return true;
	}

	/**
	* checks if c is a valid character
	* @param c - character to be tested
	* @return true if given char is valid.
	*/
	private boolean checkIfValidChar(char c)
	{
		final char[] possibleChars = {'X', 'I', 'H', 'A', 'G', 'O', 'k', 'S', ' '};

		for(int i = 0; i < possibleChars.length; i++)
			if(c == possibleChars[i])
				return true;

		return false;
	}

	/**
	* @return the number of cols or rows.
	*/
	public int getSideLength()
	{
		return this.sideLength;
	}

	/**
	* sets the sideLength parameter to x
	* @param x - the number of cols or rows
	*/
	private void setSideLength(int x)
	{
		this.sideLength = x;
	}

	/**
	 * @param characterSymbol - symbol to locate in matrix.
	 * @return where the given char is in the matrix.
	 */
	public Coordinates getCharCoordinates(char characterSymbol)
	{
		for(int y = 0; y < this.matrix.length; y++)
		{
			for(int x = 0; x < this.matrix[y].length; x++)
			{
				if(this.matrix[y][x] == characterSymbol) {
					return new Coordinates(x, y);
				}
			}
		}

		return new Coordinates(this.matrix.length, this.matrix.length);
	}

	/**
	* @param  x - x coordinate
	* @param  y - y coordinate
	* @return The char value at (x,y) of the matrix.
	*/
	public char getElementAtPosition(int x, int y)
	{
		if(x >= this.sideLength || y >= this.sideLength || x < 0 || y < 0)
			return 'E';
		else
			return this.matrix[y][x];
	}

	/**
	* Puts character c in (x,y) matrix position
	* @param x  - x coordinate
	* @param y  - y coordinate
	* @param c  - character to be inserted
	*/
	public void setCharacterToPostion(int x, int y, char c)
	{
		if(x >= this.sideLength || y >= this.sideLength || x < 0 || y < 0 || !this.checkIfValidChar(c))
			return;
		else
			this.matrix[y][x] = c;
	}

	/**
	* Signals the matrix that player has picked the key.
	*/
	public void playerHasReachedTheKey()
	{
		this.openDoors();

		this.doorsAreOpened = true;
	}

	/**
	* Changes all doors representation to 'S'.
	*/
	public void openDoors()
	{
		for(int y = 0; y < this.getSideLength(); y++)
			for(int x = 0; x < this.getSideLength(); x++)
				if(this.getElementAtPosition(x, y) == 'I')
					this.setCharacterToPostion(x, y, 'S');
	}
	
	/**
	* @return a clone of the object.
	*/
	public GameMatrix clone()
	{
		GameMatrix clone = new GameMatrix();
		
		clone.matrix = this.matrix.clone();
		
		clone.sideLength = this.sideLength;
		
		clone.doorsAreOpened = this.doorsAreOpened;
		
		return clone;
	}
}
