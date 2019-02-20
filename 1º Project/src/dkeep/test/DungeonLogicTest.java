package dkeep.test;

import static org.junit.Assert.*;
import org.junit.Test;

import dkeep.logic.*;

public class DungeonLogicTest
{
	String[] map = {
		"XXXXX",
		"XH  X",
		"I   X",
		"Ik  X",
		"XXXXX"
	};

	@Test
	public void testMoveHeroIntoFreeCell()
	{
		Level testLevel = new Level(map, 1);

		GameLogic Game = new GameLogic(testLevel);

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Player has not moved to right coords", new Coordinates(1,2), Game.getPlayerCoordinates());
	}
	
	@Test
	public void gameMatrixTests()
	{
		GameLogic Game = new GameLogic();
		
		Game.populateLevels();
		
		Game.updateGameLogicLevel();
		
		String gamemap = Game.getGameMap();
		
		char[][] mapgame = Game.getGameMatrix();
		
		int numberOfLines = mapgame.length;
		
		int numberOfRows = mapgame.length * 2;
		
		int numberOfEndLines = mapgame.length;
		
		assertEquals("Matrix its not a square!", mapgame.length, mapgame[0].length);
		
		assertEquals("String has wrong size", gamemap.length(), numberOfLines * numberOfRows + numberOfEndLines);
	}

	@Test
	public void testTryMoveHeroToWall()
	{
		Level testLevel = new Level(map, 1);

		GameLogic Game = new GameLogic(testLevel);

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates() );

		Game.insertCommand('a');

		assertEquals("Player has not moved to right coords", new Coordinates(1,1), Game.getPlayerCoordinates());
	}

	@Test
	public void testIfGameEndsWithRookie()
	{
		Level testLevel = new Level(map, 1);

		Rookie rook = new Rookie(new Coordinates(3, 1), 'G');

		testLevel.addGuard(rook);

		GameLogic Game = new GameLogic(testLevel);

		assertTrue(Game.isGameOn());

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('d');

		assertEquals("Player has not moved to right coords", new Coordinates(2,1), Game.getPlayerCoordinates());

		assertFalse("Game is still going!", Game.isGameOn());

		assertEquals("Game didn t end by Guard!", 'G', Game.getGameEndStatus());
	}

	@Test
	public void testIfGameEndsWithDrunken()
	{
		Level testLevel = new Level(map, 1);

		Drunken Drunk = new Drunken(new Coordinates(3, 1), 'G', new char[1]);

		testLevel.addGuard(Drunk);

		GameLogic Game = new GameLogic();

		Game.goToLevel(testLevel);

		assertTrue(Game.isGameOn());

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('d');

		assertEquals("Player has not moved to right coords", new Coordinates(2,1), Game.getPlayerCoordinates());

		if(Drunk.isGuardSleeping())
		{
			assertTrue("Game must continue!", Game.isGameOn());
		}
		else
		{
			assertFalse("Game is still going!", Game.isGameOn());

			assertEquals("Game didn t end by Guard!", 'G', Game.getGameEndStatus());
		}
	}

	@Test
	public void testIfGameEndsWithSuspicious()
	{
		Level testLevel = new Level(map, 1);

		Suspicious suspiciousGuard = new Suspicious(new Coordinates(3, 1), 'G', new char[1]);

		testLevel.addGuard(suspiciousGuard);

		GameLogic Game = new GameLogic();

		Game.goToLevel(testLevel);

		assertTrue(Game.isGameOn());

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('d');

		assertEquals("Player has not moved to right coords", new Coordinates(2,1), Game.getPlayerCoordinates());

		assertFalse("Game is still going!", Game.isGameOn());

		assertEquals("Game didn t end by Guard!", 'G', Game.getGameEndStatus());
	}

	@Test
	public void HeroAttemptToLeave()
	{
		Level testLevel = new Level(map, 1);

		GameLogic Game = new GameLogic();

		Game.goToLevel(testLevel);

		Game.insertCommand('s');

		Coordinates beforeAttemptCoordinates = Game.getPlayerCoordinates();

		Game.insertCommand('a');

		Coordinates afterAttemptCoordinates = Game.getPlayerCoordinates();

		assertEquals("Player was able to move through door", beforeAttemptCoordinates, afterAttemptCoordinates);
	}

	@Test
	public void testIfDoorsOpenWhenHeroIsAboveLever()
	{
		Level testLevel = new Level(map, 1);

		GameLogic Game = new GameLogic();

		Game.goToLevel(testLevel);

		Game.insertCommand('s');

		Game.insertCommand('s');

		assertEquals("Door at (0,2) hasn t openned!", Game.getElementFromGameMatrix(0, 2), 'S');

		assertEquals("Door at (0,3) hasn t openned!", Game.getElementFromGameMatrix(0, 3), 'S');
	}

	@Test
	public void testIfHeroMovesToOpenDoorsAndProgressKeep()
	{
		Level testLevel = new Level(map, 1);

		GameLogic Game = new GameLogic(testLevel.clone());
		
		Game.addLevel(testLevel.clone());

		Game.insertCommand('s');

		Game.insertCommand('s');

		Game.insertCommand('a');

		assertEquals("Did not go to keep level!", Game.getCurrentLevel(), 1);
	}

	@Test
	public void testDungeonLevelWithRookie()
	{
		GameLogic Game = new GameLogic();

		Game.populateLevels();
		
		Game.updateGameLogicLevel();

		String heroCommands = "ddssssssdwdddddssadwwaaaaaaaaa";

		for(char c: heroCommands.toCharArray())
			Game.insertCommand(c);

		assertEquals("Did not go to keep level!", Game.getCurrentLevel(), 1);
	}

	@Test
	public void testDungeonLevelWithDrunken()
	{
		GameLogic Game = new GameLogic();

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

			char [] guardRoutine = {'a', 's', 's', 's',
			's', 'a', 'a', 'a',
			'a', 'a', 'a', 's',
			'd', 'd', 'd', 'd',
			'd', 'd', 'd', 'w',
			'w', 'w', 'w', 'w'};

			//creates first level object
			Level dungeonLevel = new Level(map1, 1);

			Drunken guard = new Drunken(new Coordinates(8, 1), 'G', guardRoutine);

			dungeonLevel.addGuard(guard);

			Game.addLevel(dungeonLevel);
			
			assertTrue(Game.initializeGameController());
			
			Game.updateGameLogicLevel();

			String heroCommands = "ddssssssdwdddddssadwwaaaaaaaaa";

			for(char c: heroCommands.toCharArray())
				Game.insertCommand(c);

			if(Game.isGameOn())
				fail("Game must end!");
			else
			{
				if(Game.getGameEndStatus() == 'V')
				{
					assertEquals("Player is not at correct position.", Game.getPlayerCoordinates(), new Coordinates(0, 6));

					assertEquals("Player representation must be 'K' !", Game.getPlayerActiveRepresentationChar(), 'K');
				}
				else if(Game.getGameEndStatus() == 'G')
					assertTrue(!Game.getPlayerCoordinates().equals(new Coordinates(0, 6)));
				else
					fail("IF game is finished, its status should be 'V' or 'G' !");
			}
		}

		@Test
		public void testDungeonLevelWithSuspicious()
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

				char [] guardRoutine = {'a', 's', 's', 's',
				's', 'a', 'a', 'a',
				'a', 'a', 'a', 's',
				'd', 'd', 'd', 'd',
				'd', 'd', 'd', 'w',
				'w', 'w', 'w', 'w'};

				//creates first level object
				Level dungeonLevel = new Level(map1, 1);

				Suspicious guard = new Suspicious(new Coordinates(8, 1), 'G', guardRoutine);

				dungeonLevel.addGuard(guard);

				GameLogic Game = new GameLogic(dungeonLevel);

				String heroCommands = "ddssssssdwdddddssadwwaaaaaaaaa";

				for(char c: heroCommands.toCharArray())
					Game.insertCommand(c);

				if(Game.isGameOn())
					fail("Game must end!");
				else
				{
					if(Game.getGameEndStatus() == 'V')
					{
						assertEquals("Player is not at correct position.", Game.getPlayerCoordinates(), new Coordinates(0, 6));

						assertEquals("Player representation must be 'K' !", Game.getPlayerActiveRepresentationChar(), 'K');
					}
					else if(Game.getGameEndStatus() == 'G')
						assertTrue(!Game.getPlayerCoordinates().equals(new Coordinates(0, 6)));
					else
						fail("IF game is finished, its status should be 'V' or 'G' !");
				}
			}
		}
