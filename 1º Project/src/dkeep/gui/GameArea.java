package dkeep.gui;
import dkeep.logic.GameLogic;
import dkeep.logic.Level;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GameArea extends JPanel implements MouseListener, MouseMotionListener 
{
	private static final long serialVersionUID = 1L;
	private DungeonKeep window;
	private GameLogic game;
	private int width;
	private int height;
	private int wLength;
	private int hLength;
	private boolean createMode;
	private MapCreator creation;
	
	TreeMap<String, BufferedImage> gameElementImages;
	
	public GameArea(int width, int height, DungeonKeep window)
	{
		this.width = width;
		
		this.height = height;
		
		this.window = window;
		
		createMode = false;
		
		gameElementImages = new TreeMap<String, BufferedImage>();
		
		creation = null;
		
		loadImages();
	}
	
	public void setCreateMode(boolean state)
	{
		createMode = state;
	}
	
	public BufferedImage getImage(String filename)
	{
		BufferedImage x = null;
		URL urlToImage = null;
		
		try
		{
			urlToImage = this.getClass().getResource("./resources/" + filename);
			x = ImageIO.read(urlToImage);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return x;
	}
	
	public void loadImages()
	{	
		String[] gameElements = { "H", "K", "A",
				                      "G", "g", "O",
				                      "8", "*", "k",
				                      "I", "S", "X", "E" };
		
		String[] files = { "hero.jpg", "heroWithKey.jpg", "armedHero.jpg",
				               "guard.jpg", "sleepingGuard.jpg","ogre.png",
				               "stunnedOgre.jpg", "club.jpg", "keys.jpg",
				               "closedDoors.png", "openDoors.jpg", "wall.jpg", "emptySpace.jpg"};	
				               
		
		for(int i = 0; i < gameElements.length; i++)
			gameElementImages.put(gameElements[i], getImage(files[i]));
	}
	
	public void setGame(GameLogic g)
	{
		this.game = g;
	}
	
	public void setElement(char element)
	{
		creation.setSelectedGameElement(element);
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void processMouseEvent(int row, int line, MouseEvent m)
	{
		switch(m.getButton())
		{
		case MouseEvent.BUTTON1:
			creation.setElementForMapPosition(row, line);
			break;
		case MouseEvent.BUTTON3:
			creation.resetElementForMapPosition(row, line);
			break;
		default:
			System.out.println("Invalid mouse event!");
			break;
		}
	}

	public void checkIfPlayButtonMustBeVisible()
	{
		if(creation.isMapValid())
		{
			window.setPlayLevelVisibilityTo(true);
			
			window.setSaveLevelVisibilityTo(true);
			
			creation.setCreatorMessage("Map is valid!");
		}
		else
		{
			window.setPlayLevelVisibilityTo(false);
			
			window.setSaveLevelVisibilityTo(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		int x = arg0.getX();
		int y = arg0.getY();
		
		char[][] map = creation.getMap();
		
		int topLimit = height/map.length;
		
		wLength = width / map[0].length;
		
		hLength = (height  - topLimit) / map.length;
		
		processMouseEvent(x / wLength, (y - topLimit) / hLength, arg0);
		
		checkIfPlayButtonMustBeVisible();
		
		repaint();
	}
	
	public Level getCreatedLevel()
	{
		String[] map = creation.getFinishedMap();
		
		Level userLevel;
		
		try {
			userLevel = new Level(map, 1);
		}
		catch(IllegalArgumentException e)
		{
			return new Level();
		}
		
		if(creation.doesGameElementExist('A'))
			userLevel.playerHasClub();
		
		createMode = false;
		
		return userLevel;
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void drawBackGround(Graphics g)
	{
		super.paintComponent(g); 
		
		g.setColor(Color.WHITE); 
		
		g.fillRect(0, 0, width, height);
	}
	
	public void drawGameMessage(Graphics g)
	{
		
		g.setColor(Color.BLACK);
		
		if(createMode)
		{
			g.setFont(new Font("Tahoma", Font.PLAIN, 20));
			
			g.drawString(creation.getCreatorMessage(), 10, 60);
		}	
		else if(game != null && createMode == false)
		{
			g.setFont(new Font("Tahoma", Font.PLAIN, 30));
			
			g.drawString(game.getGameMessage(), 10, 60);
		}
	}
	
	public boolean createGameCreator(int length)
	{
		try
		{
			creation = new MapCreator(length, length);
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("Map length is invalid");
			return false;
		}
		
		createMode = true;
		
		return true;
	}
	
	public void drawGameBoard(Graphics g, char[][] map)
	{
		BufferedImage element = null;
		
		Character gameElement;
		
		int topLimit = height/map.length;
	
		wLength = width / map[0].length;
		
		hLength = (height  - topLimit) / map.length;
		
		for(int y = 0; y < map.length ; y++)
		{
			for(int x = 0; x < map[y].length; x++)
			{
				gameElement = new Character(map[y][x]);
				
				element = gameElementImages.get(gameElement.toString());
				
				g.drawImage(element, x*wLength, topLimit + (y)*hLength, wLength , hLength, null);
			}
		}
	}
		
	@Override
	public void paintComponent(Graphics g)
	{
		drawBackGround(g);
		
		char [][] map = null;
		
		if(game == null && createMode == false)
			return;
		
		if(createMode)
		{
			map = creation.getMap();	
			
			drawGameMessage(g);
		}
		else if(game != null && createMode == false)
		{
			map = game.getGameMatrix();
			
			drawGameMessage(g);
		}
		
		drawGameBoard(g, map);	
	}
}
