package dkeep.gui;

import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;

import dkeep.logic.*;
import java.awt.Color;

public class DungeonKeep implements KeyListener
{
	private JFrame DungeonKeep;
	private JComboBox guardsComboBox;
	private JLabel guardsTypeLabel;
	private JButton leftButton;
	private JButton upButton;
	private JButton downButton;
	private JButton rightButton;
	private JButton exitButton;
	private JLabel numberOfOgresLabel;
	private JButton newGameButtons;
	private GameArea graph;
	private JTextField numberOfOgresTextField;
	private GameLogic game;
	private JButton createLevelButton;
	private JButton heroButton;
	private JButton armedHeroButton;
	private JButton ogreButton;
	private JButton keyButton;
	private JButton doorButton;
	private JButton wallButton;
	private JButton playLevel;
	private JButton loadButton;
	private JButton saveButton;
	private FileLoaderAndSaver fileBridge;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{	
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					DungeonKeep window = new DungeonKeep();
					
					window.DungeonKeep.setVisible(true);
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DungeonKeep()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		DungeonKeep = new JFrame();
		DungeonKeep.setTitle("Dungeon Keep");
		DungeonKeep.setBounds(100, 100, 1156, 974);
		DungeonKeep.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container window = DungeonKeep.getContentPane();
		
		window.setLayout(null);
		
		initilizeButtonsToPlayGame(window);
		
		initializeCreatorModeButtons(window);
		
		changeCreatorButtonsVisibilityTo(false);
		
		setSaveLevelVisibilityTo(false);
		
		initializeUtilities(window);
	}
	
	private void initializeCreatorModeButtons(Container window)
	{
		
		addCreateLevelButton(window);
		
		addCreateModeButtons(window);
		
		addSaveAndLoadButtons(window);
	}
	
	private void initializeUtilities(Container window)
	{
		fileBridge = new FileLoaderAndSaver(window);
		
		graph.addKeyListener(this);
		
		graph.addMouseListener(graph);
	}
	
	private void initilizeButtonsToPlayGame(Container window)
	{
		addNumberOfOgresFields(window);
		
		addGuardsPersonalityFields(window);
		
		addNewGameButton(window);
		
		addPlayerControlButtons(window);
		
		addGameArea(window);
		
		addExitButton(window);
	}
	
	private void changeGameButtonsVisibilityTo(boolean state)
	{
		upButton.setVisible(state);
		downButton.setVisible(state);
		leftButton.setVisible(state);
		rightButton.setVisible(state);
	}
	
	private ImageIcon getIconForButton(String filename, int buttonWidth, int buttonHeight)
	{
		Image img = null;
		
		try
		{
	    img = graph.getImage(filename);
	  }
		catch (Exception ex)
		{
	    System.out.println(ex);
	    System.out.println("Failed loading file!");
	  }
		
		return new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(buttonWidth, buttonHeight, java.awt.Image.SCALE_SMOOTH));
	}
	
	public void saveCreatedLevel() 
	{
		Level createdLevel = graph.getCreatedLevel();
		
		FileOutputStream f = fileBridge.getFileForSaving();
		
		try
		{
			if(!fileBridge.saveToFile(createdLevel, f))
				System.out.println("Failed to save to file!");
			else
				System.out.println("File saved successfully!");
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void loadCreatedLvevel()
	{
		FileInputStream f = fileBridge.getFileForLoading();
		
		Level loadedLevel = null;
		
		try
		{
			String[] map = fileBridge.getMapFromFile(f);
			loadedLevel = new Level(map, 1);
			game = new GameLogic(loadedLevel);
			graph.setGame(game);
			graph.setCreateMode(false);
			changeCreatorButtonsVisibilityTo(false);
			changeGameButtonsVisibilityTo(true);
			setButtonsStatus(true);
			graph.requestFocus();
			
		} 
		catch (IOException e)
		{
			System.out.println("Something went wrong while reading file!");
			e.printStackTrace();
		}
		catch(IllegalArgumentException e)
		{
			System.out.println(e.getMessage());
			System.out.println("File is corrupt!");
			return;
		}
		
		userAction();
	}
	
	private void addLoadButton(Container window)
	{
		loadButton = new JButton("Load Level");
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				loadCreatedLvevel();
			}
		});
		loadButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
		loadButton.setBounds(532, 10, 190, 50);
		window.add(loadButton);
	}
	
	private void addSaveButton(Container window)
	{
		saveButton = new JButton("Save Level");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				saveCreatedLevel();
			}
		});
		saveButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
		saveButton.setBounds(870, 120, 190, 50);
		
		window.add(saveButton);
	}
	
	private void addSaveAndLoadButtons(Container window) 
	{
		addLoadButton(window);
		
		addSaveButton(window);
	}
	
	private void addSelectHeroButton(Container window, int buttonWidth, int buttonHeight)
	{
		heroButton = new JButton(getIconForButton("hero.jpg", buttonWidth, buttonHeight));
		
		heroButton.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{					
					System.out.println("Hero element selected");
					
					graph.setElement('H');
				}
		});
		
		heroButton.setBounds(753, 120, buttonWidth, buttonHeight);
		
		window.add(heroButton);
	}
	
	private void addSelectArmedHeroButton(Container window, int buttonWidth, int buttonHeight)
	{
		armedHeroButton = new JButton(getIconForButton("armedHero.jpg", buttonWidth, buttonHeight));
		
		armedHeroButton.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{					
					System.out.println("Armed hero element selected");
					
					graph.setElement('A');
				}
		});
		
		armedHeroButton.setBounds(753, 196, buttonWidth, buttonHeight);
		
		window.add(armedHeroButton);
	}
	
	private void addSelectOgreButton(Container window, int buttonWidth, int buttonHeight)
	{
		ogreButton = new JButton(getIconForButton("ogre.png", buttonWidth, buttonHeight));
		
		ogreButton.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{					
					System.out.println("Ogre element selected");
					
					graph.setElement('O');
				}
		});
		
		ogreButton.setBounds(753, 275, buttonWidth, buttonHeight);
		
		window.add(ogreButton);
	}
	
	private void addSelectKeyButton(Container window, int buttonWidth, int buttonHeight)
	{
		keyButton = new JButton(getIconForButton("keys.jpg", buttonWidth, buttonHeight));
		
		keyButton.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{					
					System.out.println("Key element selected");
					
					graph.setElement('k');
				}
		});
		
		keyButton.setBounds(753, 527, buttonWidth, buttonHeight);
		
		window.add(keyButton);
	}
	
	private void addSelectDoorButton(Container window, int buttonWidth, int buttonHeight)
	{
		doorButton = new JButton(getIconForButton("openDoors.jpg", buttonWidth, buttonHeight));
		
		doorButton.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{					
					System.out.println("Door element selected");
					
					graph.setElement('S');
				}
		});
		
		doorButton.setBounds(753, 356, buttonWidth, buttonHeight);
		
		window.add(doorButton);
	}
	
	private void addSelectWallButton(Container window, int buttonWidth, int buttonHeight)
	{
		wallButton = new JButton(getIconForButton("wall.jpg", buttonWidth, buttonHeight));
		
		wallButton.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{					
					System.out.println("Wall element selected");
					
					graph.setElement('X');
				}
		});
		
		wallButton.setBounds(753, 441, buttonWidth, buttonHeight);
		
		window.add(wallButton);
	}
	
	private void playerClickedPlayButton()
	{
		Level userLevel = graph.getCreatedLevel();
		
		GameLogic newGame = new GameLogic(userLevel);
		
		game = newGame;
		
		graph.setGame(newGame);
		
		changeCreatorButtonsVisibilityTo(false);
		
		changeGameButtonsVisibilityTo(true);					
		
		graph.requestFocus();
		
		graph.repaint();
	}
	
	private void addPlayGameButton(Container window, int buttonWidth, int buttonHeight)
	{
		playLevel = new JButton("Play Level");
		
		playLevel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		
		playLevel.setBounds(870, 204, 190, 50);
		
		playLevel.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{					
					playerClickedPlayButton();
				}
		});
		
		window.add(playLevel);
	}
	
	private void addCreateModeButtons(Container window)
	{
		final int buttonWidth = 75;
		final int buttonHeight = 50;
		
		addSelectHeroButton(window, buttonWidth, buttonHeight);
		
		addSelectArmedHeroButton(window, buttonWidth, buttonHeight);
		
		addSelectOgreButton(window, buttonWidth, buttonHeight);
		
		addSelectKeyButton(window, buttonWidth, buttonHeight);
		
		addSelectDoorButton(window, buttonWidth, buttonHeight);
		
		addSelectWallButton(window, buttonWidth, buttonHeight);
		
		addPlayGameButton(window, buttonWidth, buttonHeight);
	}
	
	private int getMapDimensionsFromUser()
	{
		String input = new String(JOptionPane.showInputDialog("Please input map size").toString());
		
		int convertedInput = 10;
		
		try	
		{	
			convertedInput = Integer.parseInt(input);
		}
		catch(NullPointerException e)
		{			
			return 0;
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(DungeonKeep, "Numero invalido");
			
			return 0;
		}
			
		return convertedInput;
	}
	
	private void changeCreatorButtonsVisibilityTo(boolean state)
	{
		heroButton.setVisible(state);
		armedHeroButton.setVisible(state);
		ogreButton.setVisible(state);
		keyButton.setVisible(state);
		doorButton.setVisible(state);
		wallButton.setVisible(state);
		playLevel.setVisible(state);
	}
	
	public void setPlayLevelVisibilityTo(boolean state)
	{
		playLevel.setVisible(state);
	}
	
	public void setLoadLevelVisibilityTo(boolean state)
	{
		loadButton.setVisible(state);
	}
	
	public void setSaveLevelVisibilityTo(boolean state)
	{
		saveButton.setVisible(state);
	}
	
	private void createLevelButtonClicked()
	{
		int input;
		
		input = getMapDimensionsFromUser();
							
		if(graph.createGameCreator(input) == false)
			return;

		changeGameButtonsVisibilityTo(false);
		
		changeCreatorButtonsVisibilityTo(true);
		
		setPlayLevelVisibilityTo(false);
		
		setButtonsStatus(true);
		
		graph.setGame(null);
		
		graph.repaint();
	}
	
	private void addCreateLevelButton(Container window)
	{
		createLevelButton = new JButton("Create Level");
		
		createLevelButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
		
		createLevelButton.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent arg0)
				{	
					createLevelButtonClicked();
				}
			});
		
		createLevelButton.setBounds(732, 10, 190, 50);
			
		window.add(createLevelButton);
	}
	
	private void addNumberOfOgresFields(Container window)
	{
		numberOfOgresLabel = new JLabel("Number of Ogres");
		
		numberOfOgresLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		numberOfOgresLabel.setBounds(22, 19, 166, 34);
		
		window.add(numberOfOgresLabel);
		
		numberOfOgresTextField = new JTextField();
		
		numberOfOgresTextField.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		numberOfOgresTextField.setBounds(198, 24, 34, 26);
		
		numberOfOgresTextField.setColumns(10);
		
		window.add(numberOfOgresTextField);
	}
	
	private void addGuardsPersonalityFields(Container window)
	{
		guardsTypeLabel = new JLabel("Guard personality");
		
		guardsTypeLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		guardsTypeLabel.setBounds(22, 69, 166, 32);
		
		window.add(guardsTypeLabel);
		
		String[] guardTypes = {"Rookie", "Drunken", "Suspicious"};
		
		guardsComboBox = new JComboBox(guardTypes);
		
		guardsComboBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		guardsComboBox.setBounds(198, 64, 121, 34);
		
		guardsComboBox.setSelectedIndex(0);
		
		window.add(guardsComboBox);
	}
	
	private void addNewGameButton(Container window)
	{
		newGameButtons = new JButton("New Game");
		
		newGameButtons.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(!startGame())
					return;
				
				changeCreatorButtonsVisibilityTo(false);
				
				changeGameButtonsVisibilityTo(true);
				
				graph.setGame(game);
				
				userAction();
			}
		});
		
		newGameButtons.setFont(new Font("Tahoma", Font.PLAIN, 26));
		
		newGameButtons.setBounds(332, 10, 190, 50);
		
		window.add(newGameButtons);
	}
	
	private void addGameArea(Container window)
	{
		graph = new GameArea(700, 800, this);
		
		graph.setBackground(Color.WHITE);
		
		graph.setBounds(22, 120, 700, 800);
		
		window.add(graph);
		
		graph.requestFocusInWindow();
	}
	
	private void addExitButton(Container window)
	{
		exitButton = new JButton("Exit");
		
		exitButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
		
		exitButton.setBounds(932, 10, 190, 50);
		
		window.add(exitButton);
	}
	
	private void addUpButton(Container window)
	{
		upButton = new JButton("Up");
		
		upButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				clickedButton('w');
			}
		});
		
		upButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		upButton.setBounds(870, 314, 125, 50);
		
		upButton.setEnabled(false);
		
		window.add(upButton);
	}
	
	private void addRightButton(Container window)
	{
		rightButton = new JButton("Right");
		
		rightButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				clickedButton('d');
			}
		});
		
		rightButton.setFont(new Font("Tahoma", Font.PLAIN, 20));	
		
		rightButton.setBounds(949, 388, 125, 50);
		
		rightButton.setEnabled(false);
		
		window.add(rightButton);
	}
	
	private void addDownButton(Container window)
	{
		downButton = new JButton("Down");
		
		downButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				clickedButton('s');
			}
		});
		
		downButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		downButton.setBounds(870, 465, 125, 50);
		
		downButton.setEnabled(false);
		
		window.add(downButton);
	}
	
	private void addLeftButton(Container window)
	{
		leftButton = new JButton("Left");
		
		leftButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				clickedButton('a');
			}
		});
		
		leftButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		leftButton.setBounds(792, 388, 125, 50);
		
		leftButton.setEnabled(false);
		
		window.add(leftButton);
	}
	
	private void addPlayerControlButtons(Container window)
	{
		addUpButton(window);
	
		addRightButton(window);
	
		addDownButton(window);

		addLeftButton(window);
	}
	
	private void initializeStartedGame(char typeOfGuard, int numberOfOgres)
	{
		graph.setCreateMode(false);
		
		game = new GameLogic();
		
		game.populateLevels(typeOfGuard, numberOfOgres);
		
		game.updateGameLogicLevel();

		setButtonsStatus(true);
		
		game.setGameMessage("Use arrows to move the hero (H).");
		
		userAction();
	}
	
	private boolean startGame()
	{
		char typeOfGuard = ((String) guardsComboBox.getSelectedItem()).charAt(0);
		
		int numberOfOgres;
		
		try
		{
			numberOfOgres = Integer.parseInt(numberOfOgresTextField.getText());
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(DungeonKeep, "Formato invalido para o numero de Ogres");
			return false;
		}
		
		if(numberOfOgres < 1 || numberOfOgres > 5)
		{
			JOptionPane.showMessageDialog(DungeonKeep, "O numero de ogres tem de ser maior que 1 e menor que 5!");
			return false;
		}
		
		initializeStartedGame(typeOfGuard, numberOfOgres);
		
		return true;
	}
	
	//sets the button status to a given status
	private void setButtonsStatus(boolean status)
	{
		leftButton.setEnabled(status);
		rightButton.setEnabled(status);
		upButton.setEnabled(status);
		downButton.setEnabled(status);
	}
	
	private final void setEndingGameMessage(char end)
	{
		switch(end)
		{
			case 'V':
				game.setGameMessage("You have won!");
				return;
			case 'G':
				game.setGameMessage("You were caught by the Guard!");
				return;
			case 'O':
				game.setGameMessage("You were caught by the Ogre!");
				return;
			default:
				game.setGameMessage("The game ended unexpectably!");
				return;
		}
	}
	
	private void clickedButton(char command)
	{
		game.insertCommand(command);
		
		userAction();
	}
	
	private void userAction()
	{
		if(!game.isGameOn())
		{
			setButtonsStatus(false);
			
			setEndingGameMessage(game.getGameEndStatus());
		}
		
		graph.repaint();
		
		graph.requestFocusInWindow();
	}

	@Override
	public void keyPressed(KeyEvent pressedKey)
	{
		if(!game.isGameOn())
			return;
		
		switch(pressedKey.getKeyCode())
		{
		 case KeyEvent.VK_LEFT: 
			 clickedButton('a');
			 break;
		 case KeyEvent.VK_RIGHT:
			 clickedButton('d');
			 break;
		 case KeyEvent.VK_UP:
			 clickedButton('w');
			 break;
		 case KeyEvent.VK_DOWN:
			 clickedButton('s');
			 break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}