package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Contest.ContestTime;
import engine.Contest.Partial;
import engine.db.DataBaseController;
import gui.Button.TimmerButton;

public class TimerPanel extends JPanel implements MouseListener {
	
	private JCheckBox partialsOption;
	private JComboBox distanceOptions;
	private JComboBox styleOptions;
	private JComboBox sizeOptions;
	private TimmerButton timmerB;
	private String name;
	private ArrayList<String> times = new ArrayList<String>();
	private ArrayList<String> diffs = new ArrayList<String>();
	private int swimmerID = -1;
	private DataBaseController db = null;
	private Contest swimmerPreviousBestContest = null;
	private String differenceRepresentation = null;
	
	public TimerPanel (String panelName) {
		
		if(panelName == null || panelName.isEmpty()) {
			throw new IllegalArgumentException("Invalid name!");
		}
		
		setName(panelName);
		
		timmerB = TimmerButton.getInstance();
		
		timmerB.setX(432/2-50 + 40);
		
		timmerB.setY(768/2+100 +40);
		
		timmerB.setR(80);
		
		initialize();
		
		addMouseListener(this);
	}
	
	void reset() {
		times.clear();
		
		times = new ArrayList<String>();
		
		timmerB.reset();
		
		diffs.clear();
		
		diffs = new ArrayList<String>();

    differenceRepresentation = null;

    swimmerPreviousBestContest = null;
	}
	
	public boolean hasContest() {
		return timmerB.hasTimes();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	void addDistanceLabel() {
		JLabel distanceLabel = new JLabel("Select contest distance");
		distanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		distanceLabel.setBounds(110, 60, 220, 23);
		add(distanceLabel);
	}
	
	void addStyleLabel() {
		JLabel styleLabel = new JLabel("Select contest style");
		styleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		styleLabel.setBounds(125, 120, 220, 23);
		add(styleLabel);
	}
	
	void addPoolLengthLabel() {
		JLabel lengthLabel = new JLabel("Pool size");
		lengthLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lengthLabel.setBounds(250, 190, 220, 23);
		add(lengthLabel);
	}
	
	void addStyeComboBox() {
		
		String[] styles = { "Butterfly", "Backstrocke", "Breaststroke", "Freestyle", "Medley" };
		
		styleOptions = new JComboBox(styles);
		styleOptions.setFont(new Font("Tahoma", Font.PLAIN, 18));
		styleOptions.setBounds(145, 150, 125, 26);
		styleOptions.setBackground(new Color(223, 249, 251));
		add(styleOptions);
	}
	
	void addDistanceComboBox() {
		
		String[] distances = { "25", "50", "100", "200", "400", "800" };
		
		distanceOptions = new JComboBox(distances);
		distanceOptions.setFont(new Font("Tahoma", Font.PLAIN, 18));
		distanceOptions.setBounds(175, 90, 55, 23);
		distanceOptions.setBackground(new Color(223, 249, 251));
		add(distanceOptions);
	}
	
	void addPoolSizeComboBox() {
		String[] sizes = { "25", "50" };
		
		sizeOptions = new JComboBox(sizes);
		sizeOptions.setFont(new Font("Tahoma", Font.PLAIN, 18));
		sizeOptions.setBounds(340, 190, 55, 23);
		sizeOptions.setBackground(new Color(223, 249, 251));
		add(sizeOptions);
	}
	
	void addPartialsOption() {
		
		partialsOption = new JCheckBox("Register partials");
		partialsOption.setFont(new Font("Tahoma", Font.PLAIN, 20));
		partialsOption.setBounds(20, 190, 200, 23);
		partialsOption.setBackground(new Color(223, 249, 251));
		add(partialsOption);
	}
	
	private void initialize() {
		
		setBounds(100, 100, 432, 768);
		
		addDistanceLabel();
		
		addStyleLabel();
		
		addPoolLengthLabel();
		
		addStyeComboBox();
		
		addDistanceComboBox();
		
		addPoolSizeComboBox();
		
		addPartialsOption();
	}
	
	public void setTimerState(boolean state) {
		timmerB.setState(state);
	}
	
	public Contest getContest() {
		
		Contest newContest = null;
		
		if(!hasContest()) {
			return newContest;
		}
		
		ArrayList<Partial> contestPartials = timmerB.getPartials();
		
		String style = (String) styleOptions.getSelectedItem();
		
		double poolSize = Double.parseDouble((String) sizeOptions.getSelectedItem());
		
		int contestDisatance = Integer.parseInt((String) distanceOptions.getSelectedItem());
		
		int costestSuposedNumberOFPartials = (int) (contestDisatance / poolSize);
		
		if(contestPartials.size() != 1 && contestPartials.size() != costestSuposedNumberOFPartials) {
			return newContest;
		}
		
		if(contestPartials.size() == 1 && costestSuposedNumberOFPartials != 1) {
			double t = contestPartials.get(0).getConstestTime();;
			
			contestPartials.clear();
			
			for(int i = 0; i < costestSuposedNumberOFPartials; i++) {
				contestPartials.add(new Partial(t / costestSuposedNumberOFPartials));
			}
		}
		
		Date today = Calendar.getInstance().getTime();
		
		ContestOptions.SwimmingStyle contestStyle = ContestOptions.SwimmingStyle.FREESTYLE.getSwimmingStyle(style);
		
		ContestOptions.poolDimensions contestPoolDImension =  poolSize == 25 ? ContestOptions.poolDimensions.SHORT : ContestOptions.poolDimensions.LONG;
		
		newContest = new Contest(1, contestPartials, today, contestStyle, contestPoolDImension);
		
		return newContest;
	}

	public int getSwimmerID() {
		return swimmerID;
	}

	public void setSwimmerID(int swimmerID) {
		this.swimmerID = swimmerID;
	}

	public DataBaseController getDb() {
		return db;
	}

	public void setDb(DataBaseController db) {
		this.db = db;
	}
	
	public void checkIfSwimmerHasAPreviousContest() {
		swimmerPreviousBestContest = db.getContestFromSwimmer(getSwimmerID(), (String) styleOptions.getSelectedItem(), Integer.parseInt((String) sizeOptions.getSelectedItem()), Integer.parseInt((String) distanceOptions.getSelectedItem()));
		
		if(swimmerPreviousBestContest != null) {
			System.out.println("previous time: " + swimmerPreviousBestContest.getTime());
			
			for(Partial x : swimmerPreviousBestContest.getPartials()) {
				System.out.println(x.toString());
			}
		}
	}

	private String getDiffString(double actualTime, double previousTime) {
		double diff = actualTime - previousTime;

		String diffRep = null;

		if(diff < 0) {
			Partial p = new Partial(diff * -1);

			diffRep = p.negativeToString();
		}
		else {
			Partial p = new Partial(diff);

			diffRep = p.toString();
		}

		return diffRep;
	}
	
	public void addDiff() {
		double actualTime = timmerB.getDSum();
		
		double previousTimeAtPartial = 0;
		
		for(int i = 0; i < times.size()-1; i++) {
			previousTimeAtPartial += swimmerPreviousBestContest.getPartials().get(i).getConstestTime();
		}

		diffs.add(getDiffString(actualTime, previousTimeAtPartial));
		
		if(diffs.size() > 5) {
			diffs.remove(0);
		}
	}

	public void addFinalDiff() {
		double previousTime = swimmerPreviousBestContest.getConstestTime();

		double actualTime = timmerB.getDSum();

		differenceRepresentation = getDiffString(actualTime, previousTime);
	}

	public boolean showConfirmationMessage() {
    int reply = JOptionPane.showConfirmDialog(this, "Do you want to add this contest to swimmer?", "Add?", JOptionPane.YES_NO_OPTION);

    return reply == JOptionPane.YES_OPTION;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	
	@Override
	public void mouseExited(MouseEvent arg0) {}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		
		int x = arg0.getX();
		int y = arg0.getY();
		
		if(timmerB.clickedOnMe(y, x) && timmerB.isEnabled()) {
			
			if(!timmerB.isCounting()) {
				if(partialsOption.isSelected()) {
					timmerB.setNumberOfPartials(Integer.parseInt(distanceOptions.getSelectedItem().toString()), Integer.parseInt(sizeOptions.getSelectedItem().toString()));
				}
				else {
					timmerB.setNumberOfPartials(Integer.parseInt(sizeOptions.getSelectedItem().toString()), Integer.parseInt(sizeOptions.getSelectedItem().toString()));
				}
				
				if(getSwimmerID() > -1 && partialsOption.isSelected()) {
					checkIfSwimmerHasAPreviousContest();
				}
			}
			
			timmerB.click();
			
			if(!timmerB.isLastPartial()) {
				times.add(timmerB.getSum());
				
				if(times.size() > 5) {
					times.remove(0);
				}
				
				if(partialsOption.isSelected() && swimmerPreviousBestContest != null) {
					addDiff();
				}
			}
			else if(timmerB.isLastPartial()) {

				if(partialsOption.isSelected() && swimmerPreviousBestContest != null) {
					addFinalDiff();
				}

				if(getSwimmerID() > -1 && db != null) {

          if(showConfirmationMessage()) {
            if(db.addContestToSwimmer(getContest(), getSwimmerID())) {
              System.out.println("Contest added with success with timer to swimmer with id=" + getSwimmerID());
            }
            else {
              JOptionPane.showMessageDialog(this, "Time could't be added", "Error", JOptionPane.ERROR_MESSAGE);
            }

            db = null;

            setSwimmerID(-1);
          }

					reset();
				}
			}
			repaint();
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	public void printPartialsDiffs(Graphics g) {

		g.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		for(int y = 768/2 - 65, i=diffs.size()-1; i > 0; y+=30, i--) {
			int x = 432/2-27 - 180;
			
			if(diffs.get(i).charAt(0) == '-') {
				g.setColor(new Color(76, 209, 55));
			}
			else {
				g.setColor(new Color(251, 197, 49));
				x += 5;
			}
			
			g.drawString(diffs.get(i), x, y);	
		}
	}

	public void printFinalDiff(Graphics g) {
		g.setFont(new Font("Tahoma", Font.PLAIN, 25));

		int x = 432/2-37 - 170;
		int y = 768/2 - 105;

		if(differenceRepresentation.charAt(0) == '-') {
			g.setColor(new Color(76, 209, 55));
			x -= 10;
		}
		else {
			g.setColor(new Color(251, 197, 49));
		}

		g.drawString(differenceRepresentation, x, y);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 40));
		
		g.drawString(new Partial(timmerB.getDeltaTime()).toString(), 432/2-37 - 50, 768/2 - 100);

		if(differenceRepresentation != null) {
			printFinalDiff(g);
		}
		
		g.setFont(new Font("Tahoma", Font.PLAIN, 25));

		g.setColor(Color.black);
		
		int x =  432/2-27 - 30;
		
		for(int y = 768/2 - 65, i=times.size()-1; i > 0; y+=30, i--) {
			g.drawString(times.get(i),x, y);
		}

		printPartialsDiffs(g);
		
		timmerB.paint(g);
		
		if(timmerB.isCounting()) {			
			repaint();
		}		
	}
}
