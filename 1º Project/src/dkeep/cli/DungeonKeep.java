package dkeep.cli;
import java.util.*;
import dkeep.logic.GameLogic;

class DungeonKeep
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		GameLogic game = new GameLogic();
		
		game.populateLevels();
		
		game.updateGameLogicLevel();

		Scanner s = new Scanner(System.in);

		char command;

		while(game.isGameOn())
		{
			clearConsole();

			game.printGameMatrix();

			System.out.print("Insert command(W-A-S-D): ");

			command = s.next().charAt(0);

			game.insertCommand(command);
		}
		
		game.printGameMatrix();
		
		char end = game.getGameEndStatus();
		
		printGameEndingMessage(end);

		return;
	}

	public final static void clearConsole()
	{
	    System.out.print("\033[H\033[2J");
    	    System.out.flush();
	}
	
	public final static void printGameEndingMessage(char end)
	{
		switch(end)
		{
			case 'V':
				System.out.println("You have won!");
				return;
			case 'G':
				System.out.println("You were caught by the Guard!");
				return;
			case 'O':
				System.out.println("You were caught by the Ogre!");
				return;
			default:
				System.out.println("The game ended unexpectably!");
				return;
		}
	}
}
