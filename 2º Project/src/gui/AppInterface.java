package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import engine.Coach;
import engine.Contest.Contest;
import engine.db.DataBaseController;

public class AppInterface implements ActionListener, ItemListener {

	private JFrame frame;
	private JPanel cards; //a panel that uses CardLayout
	private LoginPanel Lpanel;
	private TimerPanel Tpanel;
	private SwimmersPanel Spanel;
	private ContestPanel Cpanel;
	private CardLayout cl;
	static Coach user;
	static DataBaseController db = new DataBaseController("jdbc:sqlite:./database/app.db");
	static boolean loggedIN = false;
	JMenuItem mntmLogIn;
	JMenuItem mntmTimmer;
	JMenuItem mnSwimmers;
	JMenuItem mnContests;
	JMenuItem mntmLogOff;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppInterface window = new AppInterface();
					window.frame.setVisible(true);
					window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AppInterface() {
		initialize();
	}

	private AppInterface getInstance() {
		return this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Swimmer Coach");
		frame.setBounds(100, 100, 432, 768);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Lpanel = new LoginPanel("Login", getInstance());
		Lpanel.setBackground(new Color(223, 249, 251));
		Lpanel.setLogInOption(true);
		Lpanel.setLayout(null);

		Tpanel = new TimerPanel("Timmer");
		Tpanel.setBackground(new Color(223, 249, 251));
		Tpanel.setLayout(null);

		Spanel = new SwimmersPanel("Swimmers", getInstance(), getDataBaseConnection());
		Spanel.setBackground(new Color(223, 249, 251));
		Spanel.setLayout(null);

		Cpanel = new ContestPanel("Contests", getInstance(), getDataBaseConnection());
		Cpanel.setBackground(new Color(223, 249, 251));
		Cpanel.setLayout(null);

		addMenuBarAndOptions();

		CardLayout panelsLayout = new CardLayout();

		frame.setVisible(true);
	}

	private void addMenuBarAndOptions() {

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu options = new JMenu("Menu");

		mntmLogIn = new JMenuItem("Login");
		mntmLogIn.addActionListener(this);
		options.add(mntmLogIn);

		mntmTimmer = new JMenuItem("Timmer");
		mntmTimmer.addActionListener(this);
		options.add(mntmTimmer);

		mnSwimmers = new JMenuItem("Swimmers");
		mnSwimmers.addActionListener(this);
		options.add(mnSwimmers);

		mnContests = new JMenuItem("Contests");
		mnContests.addActionListener(this);
		options.add(mnContests);

		mntmLogOff = new JMenuItem("Log off");
		mntmLogOff.addActionListener(this);
		options.add(mntmLogOff);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(this);
		options.add(mntmExit);

		enableSubMenus(false);

		menuBar.add(options);

		cards = new JPanel(new CardLayout());
		cards.setLayout(new CardLayout());
		cards.add(Lpanel, Lpanel.getName());
		cards.add(Tpanel, Tpanel.getName());
		cards.add(Spanel, Spanel.getName());
		cards.add(Cpanel, Cpanel.getName());

		frame.getContentPane().add(cards);
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		CardLayout cl = (CardLayout)(cards.getLayout());

		cl.show(cards, (String)evt.getItem());
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		cl = (CardLayout)(cards.getLayout());

		String selectedMenu = (String)evt.getActionCommand();

		if(selectedMenu ==  "Exit") {
			System.exit(0);
		}
		else if(selectedMenu == "Log off") {
			logOffUser();
		}
		else {
			if(selectedMenu == "Contests") {
				updateContestsMenu();
			}
			changeToPanel((String)evt.getActionCommand());
		}
	}
	
	public void updateContestsMenu() {
		Cpanel.reload();
	}

	private void logOffUser() {
		changeToPanel("Login");

		setLogged(false);

		Spanel.setUser(null);

		user = null;
	}

	public void setUser(Coach u) {
		this.user = u;
	}

	public void setLogged(boolean state) {
		this.loggedIN = state;

		Lpanel.setLogInOption(!state);

		Tpanel.setTimerState(state);

		enableSubMenus(state);

		if(state) {
			Spanel.setUser(user);
			Cpanel.setUser(user);
		}	
	}

	public void changeToPanel(String panelName) {
		cl.show(cards, panelName);
	}

	public void enableSubMenus(boolean state) {
		mnContests.setEnabled(state);
		mnSwimmers.setEnabled(state);
		mntmLogOff.setEnabled(state);
		mntmTimmer.setEnabled(state);
		mntmLogOff.setEnabled(state);
	}

	public DataBaseController getDataBaseConnection() {
		return db;
	}

	public boolean timerPanelHasTime() {
		return Tpanel.hasContest();
	}

	public Contest getTimerContest() {
		return Tpanel.getContest();
	}

	public void resetTimer() {
		Tpanel.reset();
	}

	public void setSwimmerToTime(int id) {
		System.out.println("Setting swimmer in function  to " + id);
		Tpanel.setSwimmerID(id);
	}

	public void giveDataBaseToTimmer() {
		Tpanel.setDb(this.db);
	}

	public void setSwimmerToSeeContests(String name) {
		Cpanel.seeContestsFromSwimmer(name);
	}
}
