package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import engine.Coach;
import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Contest.Partial;
import engine.Swimmer.Swimmer;
import engine.db.DataBaseController;
import gui.Button.AddContestButton;
import gui.Button.AddSwimmerButton;
import gui.Button.DeleteSwimmerButton;
import gui.Button.SeeContestsButton;

public class SwimmersPanel extends JPanel implements MouseListener {

	private ArrayList<Swimmer> userSwimmers = new ArrayList<Swimmer>();

	private DataBaseController dataBase;

	private Coach theUser;

	private String name;

	private static AppInterface app;

	private static AddSwimmerButton addButton = new AddSwimmerButton(360, 50, 80);

	private static DeleteSwimmerButton deleteButton = new DeleteSwimmerButton(270, 50, 80, false);

	private static AddContestButton contestButton = new AddContestButton(180, 50, 80);

	private static SeeContestsButton seeContests = new SeeContestsButton(90, 50, 80);

	private static JScrollPane scrol;

	private SwimmersView panelToScroll;

	private int selectedSwimmer = -1;

	public SwimmersPanel(String panelName, AppInterface app, DataBaseController dbc) {

		if(dbc == null) {
			throw new IllegalArgumentException("Erro de autenticacao.");
		}

		name = panelName;

		this.app = app;

		dataBase = dbc;

		initialize();
	}

	private SwimmersPanel giveInstance() {
		return this;
	}

	private void initialize() {
		setBounds(100, 100, 432, 768);

		repaint();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private void loadSwimmers() {
		userSwimmers = dataBase.getSwimmers(theUser.getUserName(), theUser.isPoolMaster());

		panelToScroll = new SwimmersView(userSwimmers);

		panelToScroll.setPanelFather(this);

		scrol = new JScrollPane(panelToScroll, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrol.setBounds(0, 100, 418, 610);

		scrol.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96)));

		add(scrol);

		addMouseListener(this);
	}

	public void setUser(Coach user) {
		theUser = user;

		if(user != null) {
			loadSwimmers();
		}
	}

	public void reload() {

		this.remove(scrol);

		loadSwimmers();

		panelToScroll.revalidate();

		panelToScroll.repaint();

		scrol.repaint();

		scrol.revalidate();

		repaint();
	}

	public String getProfilePictureName() {
		JFileChooser fileNameRetriver = new JFileChooser();

		fileNameRetriver.setDialogTitle("Select swimmer profile Picture");

		String toReturn = null;

		if(fileNameRetriver.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

			toReturn = fileNameRetriver.getSelectedFile().getName();

			File fileToMove;

			fileToMove = new File(fileNameRetriver.getSelectedFile().getAbsolutePath());

			if(fileToMove.renameTo(new File("./media/" + toReturn))) {
				System.out.println("File successfully loaded to path " + new File("./media/" + toReturn).getAbsolutePath());
			}
			else {
				System.out.println("Failed to load file, default attributed to new swimmer!");
				toReturn = "noPicture.png";
			}
		}
		else {
			toReturn = "noPicture.png";
		}

		return toReturn;
	}

	public void addSwimmerDialog() {
		String name = JOptionPane.showInputDialog("Swimmer name:");

		if(name == null) {
			System.out.println("Insertion interrupted");
			return;
		}

		String dob = JOptionPane.showInputDialog("Swimmer birth day:");

		if(dob == null || dob.length() != 10) {
			System.out.println("Insertion interrupted");
			return;
		}

		String profilePictureName = getProfilePictureName();

		if(dataBase.insertSwimmer(name, dob, profilePictureName, theUser.getId(), theUser.getPool_id())) {
			JOptionPane.showMessageDialog(this, "Swimmer added with success", "Success", JOptionPane.PLAIN_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(this, "Swimmer could't be added", "Error", JOptionPane.ERROR_MESSAGE);
		}

		reload();
	}

	public boolean addContestToSwimmer() {
		Contest newSwimmerContest = app.getTimerContest();

		if(newSwimmerContest == null) {
			return false;
		}

		if(!dataBase.addContestToSwimmer(newSwimmerContest, getCurrentlySelectedSwimmerID())) {
			System.out.println("Failed to insert contest!");
		};

		app.resetTimer();

		panelToScroll.setSelectedSwimmer(-1);

		System.out.println("Contest succesfully inserted");

		return true;
	}

	private int getCurrentlySelectedSwimmerID() {
		return userSwimmers.get(panelToScroll.getSelectedSwimmer()).getID();
	}

	private void deleteSelectedSwimmer() {
		if(dataBase.deleteSwimmer(getCurrentlySelectedSwimmerID())) {
			System.out.println("Swimmer deleted");
			panelToScroll.setSelectedSwimmer(-1);
		}

		reload();
	}

	public void getANewContestForSelectedSwimmer() {
		app.changeToPanel("Timmer");

		app.setSwimmerToTime(getCurrentlySelectedSwimmerID());

		app.giveDataBaseToTimmer();
	}

	public Date extractDateFromString(String d) {
		Date cDate = new Date();

		String[] dateElements = d.split("-");

		cDate.setYear(Integer.parseInt(dateElements[0]) - 1900);
		cDate.setMonth(Integer.parseInt(dateElements[1]) - 1);
		cDate.setDate(Integer.parseInt(dateElements[2]));

		return cDate;
	}

	private int getSelectedOption(String message, String title, Object[] options) {
		return JOptionPane.showOptionDialog(this,
				message,
				title,
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
	}

	public void addOldContestProcedure() {
		Object[] styles = { "Butterfly", "Backstrocke", "Breaststroke", "Freestyle", "Medley" };

		Object[] poolSizes = { 25, 50 };

		Object[] contestLengthOptions = { 25, 50, 100, 200, 400, 800 };

		int poolDimensionIndex;

		int contestDistanceIndex;

		String contestDate;

		String contestTime;

		Object selectStyle = JOptionPane.showInputDialog(this,
				"Choose the contest style",
				"Contest style selection",
				JOptionPane.INFORMATION_MESSAGE,
				null,
				styles,
				styles[0]);

		if(selectStyle == null) {
			return;
		}

		if((poolDimensionIndex = getSelectedOption("Select pool size", "Pool size", poolSizes)) == -1) {
			return;
		}

		if((contestDistanceIndex = getSelectedOption("Select contest distance", "Contest distance", contestLengthOptions)) == -1) {
			return;
		}

		if((contestDate = JOptionPane.showInputDialog("Contest date (YYYY-MM-DD) : ")) == null) {
			return;
		}

		if((contestTime = JOptionPane.showInputDialog("Contest time (MM:ss.mmm) : ")) == null) {
			return;
		}

		Partial p = null;

		int contestDistance = (int) contestLengthOptions[contestDistanceIndex];

		int poolLength = (int) poolSizes[poolDimensionIndex];

		int numberOfPartials = contestDistance / poolLength;

		try {
			p = new Partial(contestTime);
		} catch (IllegalArgumentException e) {
			System.out.println("Partial creation exception -> " + contestTime + " -> " + e.getMessage());
			return;
		}

		ArrayList<Partial> contestPartials = new ArrayList<Partial>();

		double t = p.getConstestTime();

		for(int i = 0; i < numberOfPartials; i++) {
			contestPartials.add(new Partial(t / numberOfPartials));
		}

		Contest toInsert = new Contest(0,
				contestPartials,
				extractDateFromString(contestDate),
				ContestOptions.SwimmingStyle.FREESTYLE.getSwimmingStyle((String)selectStyle),
				contestDistance == 25 ? ContestOptions.poolDimensions.SHORT :  ContestOptions.poolDimensions.LONG);

		dataBase.addContestToSwimmer(toInsert, getCurrentlySelectedSwimmerID());
	}

	public void contestButtonclicked() {
		if(app.timerPanelHasTime()) {				
			if(!addContestToSwimmer()) {
				JOptionPane.showMessageDialog(this, "Contest addition failed", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else {
			Object[] possibleValues = { "Time new contest", "Insert old contest data" };

			int selectedValue = JOptionPane.showOptionDialog(this,
					"Do you pretend to time a new contest or insert data relative to older contest?", "Old or new?",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					possibleValues, possibleValues[0]);

			if(selectedValue == 0) {
				getANewContestForSelectedSwimmer();
			}
			else if(selectedValue ==  1) {
				addOldContestProcedure();
			}
			else {
				return;
			}

		}
	}

	public void seeContestsFromSwimmer() {
		app.setSwimmerToSeeContests(userSwimmers.get(panelToScroll.getSelectedSwimmer()).getSwimmerName());

		app.changeToPanel("Contests");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		addButton.paint(g);

		if(panelToScroll.getSelectedSwimmer() > -1) {
			deleteButton.paint(g);
			contestButton.paint(g);
			seeContests.paint(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		int y = e.getY();
		int x = e.getX();

		if(addButton.clickedOnMe(y, x)) {
			addSwimmerDialog();	
		}
		else if(deleteButton.clickedOnMe(y, x) && panelToScroll.swimmerIsSelected()) {
			deleteSelectedSwimmer();
		}
		else if(contestButton.clickedOnMe(y, x) && panelToScroll.swimmerIsSelected()) {
			contestButtonclicked();
		}
		else if(seeContests.clickedOnMe(y, x) && panelToScroll.swimmerIsSelected()) {
			seeContestsFromSwimmer();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
