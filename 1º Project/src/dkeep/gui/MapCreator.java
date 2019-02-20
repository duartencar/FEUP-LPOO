package dkeep.gui;

import java.util.ArrayList;

public class MapCreator
{
	char[][] map;
	boolean[][] visited;
	char selectedGameElement = 'E';
	String msg = "Select a game element and place them in map!";
	
	public MapCreator(int width, int height)
	{
		if(width < 4 || height < 4)
			throw new IllegalArgumentException();
		
		map = new char[width][height];
		
		visited = new boolean[width][height];

		initialize();
	}
	
	public String getCreatorMessage()
	{
		return msg;
	}
	
	public void setCreatorMessage(String msg)
	{
		this.msg = msg;
	}
	
	public void initialize()
	{
		for(int y = 0; y < map.length; y++)
		{
			for(int x = 0; x < map[0].length; x++)
			{
				map[y][x] = 'E';
				
				visited[y][x] = false;
			}
		}
	}
	
	public void resetVisited()
	{
		for(int y = 0; y < map.length; y++)
		{
			for(int x = 0; x < map[0].length; x++)
			{		
				visited[y][x] = false;
			}
		}
	}
	
	public void setElementForMapPosition(int x, int y)
	{
		if(selectedGameElement == 'H' || selectedGameElement == 'A')
		{
			if(doesGameElementExist('H') || doesGameElementExist('A'))
				return;
		}
		
		map[y][x] = selectedGameElement;
	}
	
	public void resetElementForMapPosition(int x, int y)
	{
		map[y][x] = 'E';
	}
	
	public char[][] getMap()
	{
		char[][] clone = new char[map.length][map[0].length];
		
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[0].length; j++)
			{
				clone[i][j] = map[i][j];
			}
		}
		
		return clone;
	}
	
	public void setSelectedGameElement(char element)
	{
		selectedGameElement = element;
	}
	
	public boolean doesGameElementExist(char element)
	{
		int[] position = findGameElement(element);
		
		if(position[0] > -1 && position[1] > -1)
			return true;
		
		return false;
	}
	
	public int[] findGameElement(char element)
	{
		int[] result = {-1, -1};
		
		for(int y = 0; y < map.length; y++)
		{
			for(int x = 0; x < map[0].length; x++)
			{
				if(map[y][x] == element)
				{
					result[0] = x;
					result[1] = y;
				}				
			}
		}
		
		return result;
	}
	
	public int[] getPlayerStartingCoords()
	{
		int[] playerCoords = findGameElement('H');
		
		if(playerCoords[0] == -1 || playerCoords[1] == -1)
			playerCoords = findGameElement('A');
		
		return playerCoords;
	}
	
	public boolean isTherePathTo(int x, int y, char objective)
	{
		visited[y][x] = true;
		
		System.out.println("x= " + x + " y= " + y + " element= " + map[y][x]);
		
		if(map[y][x] == objective)
		{
			System.out.println("Found at (" + x + "," + y +")!\n");
			
			return true;
		}
		else
		{
		//if element to the RIGHT hasn t been visited, and it s not a wall and is part of the map find goal there
			if(x+1 < map[0].length && !visited[y][x+1] && map[y][x+1] != 'I' && map[y][x+1] != 'X')
				if(isTherePathTo(x + 1, y, objective))
					return true;
			//if element to the LEFT hasn t been visited, and it s not a wall and is part of the map find goal there
			if(x-1 >= 0 && !visited[y][x - 1] && map[y][x - 1] != 'I' && map[y][x - 1] != 'X')
				if(isTherePathTo(x - 1, y, objective))
					return true;
			//if the DOWN element hasn t been visited, and it s not a wall and is part of the map find goal there
			if(y+1 < map.length && !visited[y+1][x] && map[y+1][x] != 'I' && map[y+1][x] != 'X')
				if(isTherePathTo(x, y + 1, objective))
					return true;
			//if the UP element hasn t been visited, and it s not a wall and is part of the map find goal there
			if(y-1 >= 0 && !visited[y-1][x] && map[y-1][x] != 'I' && map[y-1][x] != 'X')
				if(isTherePathTo(x, y - 1, objective))
						return true;
			
			//If nothing was found return false 
			return false;
		}
	}
	
	public ArrayList<Integer[]> getStairs()
	{
		ArrayList<Integer[]> coordinates = new ArrayList<Integer[]>();
		
		Integer[] location = new Integer[2];
		
		for(int x = 0; x < map[0].length; x++)
		{
			if(map[0][x] == 'S')
			{
				location[0] = x;
				location[1] = 0;
				coordinates.add(location.clone());
			}
			if(map[map.length - 1][x] == 'S')
			{
				location[0] = x;
				location[1] = map.length - 1;
				coordinates.add(location.clone());
			}
		}
		
		for(int y = 0; y < map.length; y++)
		{
			if(map[y][0] == 'S')
			{
				location[0] = 0;
				location[1] = y;
				coordinates.add(location.clone());
			}
			if(map[y][map[0].length - 1] == 'S')
			{
				location[0] = map[0].length - 1;
				location[1] = y;
				coordinates.add(location.clone());
			}
		}
			
		return coordinates;
	}
	
	public boolean isMapValid()
	{
		resetVisited();
		
		int[] playerCoords = getPlayerStartingCoords();
		
		if(playerCoords[0] == -1 || playerCoords[1] == -1)
		{
			setCreatorMessage("You must add a Player first!");
			return false;
		}
			
		if(isTherePathTo(playerCoords[0], playerCoords[1], 'k') == false)
		{
			setCreatorMessage("You must add at least one key!");
			return false;
		}
		
		resetVisited();
			
		ArrayList<Integer[]> stairsLocation = getStairs();
		
		for(Integer[] location : stairsLocation)
		{	
			if(isTherePathTo(location[0], location[1], 'k'))
				return true;
			
			resetVisited();
		}
		
		setCreatorMessage("Door not found!");
		
		return false;
	}

	public String[] getFinishedMap()
	{
		String[] finishedMap = new String[map.length];
		
		String mapLine = "";
		
		for(int y = 0; y < map.length; y++)
		{
			for(int x = 0; x < map[0].length; x++)
			{
				if(map[y][x] == 'E')
					map[y][x] = ' ';
				else if(map[y][x] == 'S')
					map[y][x] = 'I';
				
				mapLine += map[y][x];
			}
			
			finishedMap[y] = mapLine;
			
			mapLine = "";
		}
		
		return finishedMap;
	}
}
