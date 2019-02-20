package gui.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import engine.Contest.Partial;

public class TimmerButton extends TimmerButtonDesign {

	private static TimmerButton uniqueInstance;

	private int numberOfClicks = 0;

	private boolean enabled = false;

	private boolean counting = false;

	int numberOfPartialsToCount = 1;

	private long startTime; 

	private long endTime = 0; 

	private ArrayList<Partial> ContestPartials = new ArrayList<Partial>();

	private float sum = 0;

	private Partial aux = new Partial();

	private TimmerButton() {
	}

	public static TimmerButton getInstance() {
		if(uniqueInstance == null) {
			uniqueInstance = new TimmerButton();
		}

		return uniqueInstance;
	}

	public void reset() {
		uniqueInstance.aux = new Partial();

		uniqueInstance.ContestPartials.clear();

		uniqueInstance.ContestPartials = new ArrayList<Partial>();

		uniqueInstance.sum = 0;

		uniqueInstance.startTime = 0;

		uniqueInstance.endTime = 0;

		uniqueInstance.numberOfPartialsToCount = 1;

		uniqueInstance.counting = false;

		uniqueInstance.numberOfClicks = 0;

		uniqueInstance.textIndex = 0;

		setState(true);
	}

	public boolean hasTimes() {
		return ContestPartials.size() > 0;
	}

	public int getNumberOfPartials() {
		return ContestPartials.size();
	}

	public ArrayList<Partial> getPartials() {
		return ContestPartials;
	}

	public boolean isCounting() {
		return counting;
	}

	public void setCounting(boolean counting) {
		this.counting = counting;
	}

	public void setState (boolean state) {
		enabled = state;

		if(enabled) {
			colorIndex = 1;
		}
		else {
			colorIndex = 0;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void start() {
		counting = true;
		colorIndex++;
		textIndex++;
		startTime = System.currentTimeMillis();
	}

	public float getDeltaTime() {

		float t = 0;

		if(isCounting()) {
			t = System.currentTimeMillis() - startTime;
		}
		else {
			if(endTime != 0) {
				t = endTime - startTime;
			}
		}

		return t / 1000;
	}

	public boolean isLastPartial() {
		return numberOfPartialsToCount - numberOfClicks == 0;
	}

	private void end() {
		counting = false;

		endTime = System.currentTimeMillis();

		setState(false);
	}

	public void setNumberOfPartials (int meters, int poolSize) {
		numberOfPartialsToCount = meters / poolSize;
	}

	public long getStartTime() {
		return startTime;
	}

	private void addPartial(long time) {

		double partialTime = 0;

		partialTime = time - startTime;

		if(ContestPartials.size() == 0) {

			ContestPartials.add(new Partial(partialTime / 1000));
		}
		else {			
			partialTime -= sum;

			ContestPartials.add(new Partial(partialTime / 1000));
		}

		sum += partialTime;	
	}

	public void click() {
		if(!isCounting() && numberOfClicks < numberOfPartialsToCount) {
			if(numberOfClicks == numberOfPartialsToCount - 1) {
				colorIndex++;
				textIndex++;
			}
			start();
		}
		else {
			numberOfClicks++;

			addPartial(System.currentTimeMillis());

			if(numberOfClicks == numberOfPartialsToCount - 1) {
				colorIndex++;
				textIndex++;
			}
			else if(numberOfClicks == numberOfPartialsToCount) {
				end();
			}
		}
	}

	public boolean isUserSeeingFinalResult() {
		return numberOfClicks > numberOfPartialsToCount;
	}

	public String getSum() {
		aux.setContestTime(sum / 1000);

		return aux.toString();
	}

	public double getDSum() {
		aux.setContestTime(sum / 1000);

		return aux.getConstestTime();
	}

	public void paint(Graphics g) {
		g.setColor(getColor());

		g.fillOval(432/2-50, 768/2+100, 80, 80);

		g.setColor(Color.white);

		g.setFont(new Font("Tahoma", Font.PLAIN, 20));

		g.drawString(getText(), 432/2-37 + getStringAllignment(), 768/2+150);
	}
}
