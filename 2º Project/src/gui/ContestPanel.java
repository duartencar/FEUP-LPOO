package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import engine.Coach;
import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Swimmer.Swimmer;
import engine.db.DataBaseController;

public class ContestPanel extends JPanel implements MouseListener {

	private ArrayList<Swimmer> userSwimmers = new ArrayList<Swimmer>();

	private DataBaseController dataBase;

	private Coach theUser;

	private String name;

	private static AppInterface app;

	private static JScrollPane scrol;

	private ContestsView panelToScrol;

	private JComboBox distanceOptions;
	private JComboBox styleOptions;
	private JComboBox sizeOptions;
	private JComboBox swimmerOption;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContestPanel(String panelName, AppInterface app, DataBaseController dbc) {

		if(dbc == null) {
			throw new IllegalArgumentException("Erro de autenticacao.");
		}

		name = panelName;

		this.app = app;

		dataBase = dbc;

		initialize();
	}

	private void initialize() {
		setBounds(100, 100, 432, 768);

		repaint();
	}

	public void setUser(Coach user) {
		theUser = user;

		if(user != null) {
			loadSwimmersAndContests();
		}
	}

	private void addScrollingPanel() {
		panelToScrol = new ContestsView(userSwimmers);

		panelToScrol.setPanelFather(this);

		scrol = new JScrollPane(panelToScrol, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrol.setBounds(0, 100, 418, 610);

		scrol.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96)));

		add(scrol);
	}

	void addDistanceComboBox() {

		String[] distances = { "All", "25", "50", "100", "200", "400", "800" };

		distanceOptions = new JComboBox(distances);
		distanceOptions.setFont(new Font("Tahoma", Font.PLAIN, 18));
		distanceOptions.setBounds(180, 25, 55, 23);
		distanceOptions.setBackground(new Color(223, 249, 251));
		distanceOptions.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				panelToScrol.repaint();
			}});
		add(distanceOptions);
	}

	void addStyeComboBox() {

		String[] styles = { "All", "Butterfly", "Backstrocke", "Breaststroke", "Freestyle", "Medley" };

		styleOptions = new JComboBox(styles);
		styleOptions.setFont(new Font("Tahoma", Font.PLAIN, 18));
		styleOptions.setBounds(5, 25, 125, 26);
		styleOptions.setBackground(new Color(223, 249, 251));
		styleOptions.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				panelToScrol.repaint();
			}});
		add(styleOptions);
	}

	void addPoolSizeComboBox () {
		String[] sizes = { "All" , "25", "50" };

		sizeOptions = new JComboBox(sizes);
		sizeOptions.setFont(new Font("Tahoma", Font.PLAIN, 18));
		sizeOptions.setBounds(300, 25, 55, 23);
		sizeOptions.setBackground(new Color(223, 249, 251));
		sizeOptions.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				panelToScrol.repaint();
			}});
		add(sizeOptions);
	}

	void seeContestsFromSwimmer(String swimmerName) {
		swimmerOption.setSelectedItem(swimmerName);
	}

	void addSwimmersNameComboBox () {
		String[] names = new String[userSwimmers.size() + 1];

		int i = 1;

		names[0] = "All";

		for(Swimmer s : userSwimmers) {
			names[i] = s.getSwimmerName();
			i++;
		}

		swimmerOption = new JComboBox(names);
		swimmerOption.setFont(new Font("Tahoma", Font.PLAIN, 18));
		swimmerOption.setBounds(138, 73, 157, 23);
		swimmerOption.setBackground(new Color(223, 249, 251));
		swimmerOption.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				panelToScrol.repaint();
			}});
		add(swimmerOption);

	}

	public void loadSwimmersAndContests() {
		userSwimmers = dataBase.getSwimmersWithContests(theUser.getUserName(), theUser.isPoolMaster());

		addScrollingPanel();

		addStyeComboBox();

		addDistanceComboBox();

		addPoolSizeComboBox();

		addSwimmersNameComboBox();

		addMouseListener(this);

		System.out.println(userSwimmers.size() + " swimmers were loaded");
	}
	
	public void reload( ) {
		this.remove(scrol);
		
		userSwimmers = dataBase.getSwimmersWithContests(theUser.getUserName(), theUser.isPoolMaster());

		addScrollingPanel();
		
		panelToScrol.revalidate();

		panelToScrol.repaint();

		scrol.repaint();

		scrol.revalidate();

		repaint();
	}

	public boolean checkIfContestMustBeDisplayed(Contest x, Swimmer s) {

		String selected = (String)swimmerOption.getSelectedItem();

		if(selected != "All" && !selected.equals(s.getSwimmerName())) {
			return false;
		}

		if(x.getStyle() != ContestOptions.SwimmingStyle.BACKSTROKE.getSwimmingStyle((String) styleOptions.getSelectedItem()) && (String) styleOptions.getSelectedItem() != "All") {
			System.out.println("Selected= " + (String) styleOptions.getSelectedItem() + " contest=" + x.getStyle().getTypeName());
			return false;
		}

		if((String)distanceOptions.getSelectedItem() != "All" && x.getContestDistance() != Integer.parseInt((String)distanceOptions.getSelectedItem())) {
			return false;
		}

		if((String)sizeOptions.getSelectedItem() != "All" && x.getPoolLength().getLenght() != Integer.parseInt((String)sizeOptions.getSelectedItem())) {
			return false;
		}

		return true;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setFont(new Font("Tahoma", Font.PLAIN, 18));

		g.setColor(new Color(39, 174, 96));

		g.drawString("Style", 50, 20);

		g.drawString("Distance", 175, 20);

		g.drawString("Pool size", 295, 20);

		g.drawString("Swimmer", 180, 68);

	}
}
