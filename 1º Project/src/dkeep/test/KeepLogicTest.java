package dkeep.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import dkeep.logic.*;

public class KeepLogicTest
{
	String[] map = {
		"XXXXX",
		"XH  X",
		"I   X",
		"Ik  X",
		"XXXXX"
	};

	@Test
	public  void testIfOgreCatchesHero()
	{
		Level testLevel = new Level(map, 1);

		testLevel.addOgre(new Coordinates(3,1), 'O');

		testLevel.setExtraStep();

		GameLogic Game = new GameLogic(testLevel);

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('d');

		assertEquals("Failed to get right player coords", new Coordinates(2,1), Game.getPlayerCoordinates());

		assertFalse(Game.isGameOn());

		assertEquals(Game.getGameEndStatus(), 'O');
	}

	@Test
	public  void testIfHeroChangesHisRepresentation()
	{
		Level testLevel = new Level(map, 1);

		testLevel.addOgre(new Coordinates(3,1), 'O');

		testLevel.setExtraStep();

		GameLogic Game = new GameLogic(testLevel);

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Failed to get right player coords", new Coordinates(1,2), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Failed to get right player coords", new Coordinates(1,3), Game.getPlayerCoordinates());

		assertEquals(Game.getPlayerActiveRepresentationChar(), 'K');
	}

	@Test
	public void testIfHeroChangesToArmedMode()
	{
		Level keepLevel = new Level(map, 1);

		keepLevel.addOgre(new Coordinates(3,1), 'O');

		keepLevel.setExtraStep();

		keepLevel.playerHasClub();

		GameLogic Game = new GameLogic(keepLevel);

		assertEquals("Player representation is wrong!", 'A', Game.getPlayerActiveRepresentationChar());
	}

	@Test
	public void testIfHeroStunsOgre()
	{
		Level keepLevel = new Level(map, 1);

		Ogre testOgre = new Ogre(new Coordinates(3,1), 'O');

		keepLevel.addOgre(testOgre);

		keepLevel.setExtraStep();

		keepLevel.playerHasClub();

		GameLogic Game = new GameLogic(keepLevel);

		assertEquals("Player representation is wrong!", 'A', Game.getPlayerActiveRepresentationChar());

		Game.insertCommand('d');

		assertTrue(testOgre.isOgreStunned());

		assertEquals("Ogre representation didn t change!", testOgre.getActiveChar(), '8');

		assertTrue(Game.isGameOn());

		assertEquals("That game status is wrong!", Game.getGameEndStatus(), '*');

		Game.insertCommand('d');

		assertTrue(testOgre.isOgreStunned());

		Game.insertCommand('d');
		
		assertTrue(testOgre.isOgreStunned());
	}

	@Test
	public  void heroAttemptsToLEaveWithoutKey()
	{
		Level testLevel = new Level(map, 1);

		testLevel.addOgre(new Coordinates(3,1), 'O');

		testLevel.setExtraStep();

		GameLogic Game = new GameLogic(testLevel);

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Failed to get right player coords", new Coordinates(1,2), Game.getPlayerCoordinates());

		Game.insertCommand('a');

		assertEquals("Player moved to closed door", new Coordinates(1,2), Game.getPlayerCoordinates());
	}

	@Test
	public  void heroMovesToDoorWithKey()
	{
		Level testLevel = new Level(map, 1);

		testLevel.addOgre(new Coordinates(3,1), 'O');

		testLevel.setExtraStep();

		GameLogic Game = new GameLogic(testLevel);

		assertTrue(Game.doesPlayerNeedAnotherStep());

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Failed to get right player coords", new Coordinates(1,2), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Failed to get right player coords", new Coordinates(1,3), Game.getPlayerCoordinates());

		Game.insertCommand('a');

		//check if players hasn t moved
		assertEquals("Player moved to closed door " + Game.getPlayerCoordinates(), new Coordinates(1,3), Game.getPlayerCoordinates());

		//check if doors have oppened
		assertEquals("Doors haven t oppened: " + Game.getElementFromGameMatrix(0, 3), 'S', Game.getElementFromGameMatrix(0, 3));
	}

	@Test
	public  void heroWinsGame()
	{
		Level testLevel = new Level(map, 1);

		testLevel.addOgre(new Coordinates(3,1), 'O');

		testLevel.setExtraStep();

		GameLogic Game = new GameLogic(testLevel);

		assertTrue(Game.doesPlayerNeedAnotherStep());

		assertEquals("Failed to get right player coords", new Coordinates(1,1), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Failed to get right player coords", new Coordinates(1,2), Game.getPlayerCoordinates());

		Game.insertCommand('s');

		assertEquals("Failed to get right player coords", new Coordinates(1,3), Game.getPlayerCoordinates());

		Game.insertCommand('a');

		//check if players hasn t moved
		assertEquals("Player moved to closed door " + Game.getPlayerCoordinates(), new Coordinates(1,3), Game.getPlayerCoordinates());

		//check if doors have oppened
		assertEquals("Doors haven t oppened: " + Game.getElementFromGameMatrix(0, 3), 'S', Game.getElementFromGameMatrix(0, 3));

		Game.insertCommand('a');

		//check if players hasn t moved
		assertEquals("Player hasn t moved to open door", new Coordinates(0,3), Game.getPlayerCoordinates());

		assertFalse(Game.isGameOn());

		assertEquals("Game didnt end with victory!", Game.getGameEndStatus(), 'V');
	}

	@Test(timeout=1000)
	public void testSomeRandomBehavior()
	{
		String[] keepMap = {
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

			String playerMoves = "dddddddwwwwwwwaaaaaaaaa";

			Level keepLevel = new Level(keepMap, 1);

			ArmedOgre newOgre = new ArmedOgre(new Coordinates(4, 1), 'O');

			keepLevel.addArmedOgre(newOgre);

			keepLevel.setExtraStep();

			keepLevel.playerHasClub();

			GameLogic Game = new GameLogic(keepLevel);

			assertTrue(Game.doesPlayerNeedAnotherStep());

			assertTrue(Game.canPlayerFight());

			assertTrue(Game.isGameOn());

			for(int i = 0; i < playerMoves.length() && Game.isGameOn(); i++)
			  Game.insertCommand(playerMoves.charAt(i));

			if(Game.isGameOn())
				fail("Game is still on!" + Game.getPlayerCoordinates());
			else if(Game.getGameEndStatus() == '*')
				fail("End status didnt change");
			else if(Game.getGameEndStatus() == 'V')
				assertEquals("Player won without going to dungeon door", Game.getPlayerCoordinates(), new Coordinates(0, 1));
			else if(Game.getGameEndStatus() == 'O')
				assertFalse(Game.getPlayerCoordinates().equals(new Coordinates(0, 1)));
			else
				fail("Not tested " + Game.getPlayerCoordinates());
		}
	}
