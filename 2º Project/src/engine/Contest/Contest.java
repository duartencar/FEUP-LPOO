package engine.Contest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Date;

import engine.Swimmer.Swimmer;

/**
 * Class that holds all contest information
 * @see ContestTime
 * @see Partial
 */
public class Contest extends ContestTime {
	private int id;
	private Date date;
	private ContestOptions.SwimmingStyle style;
	private ContestOptions.poolDimensions poolLength;

	 /**
	 * Constructor of Contest CLass
	 * @param id - contest id
	 * @param partials - all contest Partials
	 * @param contestDate - contest date
	 * @param style - contest style
	 * @param poolLength - length of the pool were contest was held
	 * @throws IllegalArgumentException if one of the parameters is null
	 */
	public Contest(int id, ArrayList<Partial> partials, Date contestDate, ContestOptions.SwimmingStyle style, ContestOptions.poolDimensions poolLength) throws IllegalArgumentException {

		super(partials);

		if(contestDate == null || style == null || poolLength == null) {
			throw new IllegalArgumentException("Contest date or style is NULL!");
		}

		this.date = contestDate;

		this.style = style;

		this.poolLength = poolLength;

		this.id = id;
	}

	/**
	 * @return contest date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Changes contest date to given date
	 * @param date - date for contest
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return pool length were the contest was held
	 */
	public ContestOptions.poolDimensions getPoolLength() {
		return poolLength;
	}

	/**
	 * @return integer  pool length were the contest was held
	 */
	public int getIntPoolLength() {
		return poolLength.getLenght();
	}

	/**
	 * default constructor of Contest CLass
	 */
	public Contest() {

		this.contestPartials = null;

		this.date = null;

		this.style = null;

		this.poolLength = null;
	}

	/**
	 * Contest total distance based on number of partials and pool length
	 * @return contest distance
	 */
	public int getContestDistance() {
		return getNumberOfPartials() * (int)poolLength.getLenght();
	}

	/**
	 * @return contest time representation
	 */
	public String getTime() {
		return super.toString();
	}

	/**
	 * @return contest style
	 */
	public ContestOptions.SwimmingStyle getStyle() {
		return style;
	}

	/**
	 * @return contest id
	 */
	public int getId() {
		return id;
	}

	/**
	 * implements toString method
	 * @return string with distance, style and date
	 */
	@Override
	public String toString() {
		return "" + getContestDistance() + " m " + style.getTypeName() + "  " + getDateRepresentation();
	}

	/**
	 * gets a printable string representation of contest date
	 * @return contest date in format YYYY-MM-DD
	 */
	@SuppressWarnings("deprecation")
	public String getDateRepresentation() {
		return String.format("%d-%02d-%02d", date.getYear() + 1900, date.getMonth()+1, date.getDate());
	}

	/**
	 * implements clone method
	 * @return object clone
	 */
	public Contest clone() {

		return new Contest(id, (ArrayList<Partial>)contestPartials.clone(), (Date) date.clone(), style, poolLength);
	}

	/**
	 * @return contest partials
	 */
	public ArrayList<Partial> getPartials() {
		return contestPartials;
	}

	/**
	 * paints contest info to a container
	 * @param g - container
	 * @param s - swimmer that this contest belongs to
	 * @param y - y value were to start drawing
	 * @param x - x value were to start drawing
	 */
	public void paint(Graphics g, Swimmer s, int y, int x) {
		g.drawImage(s.getProfilePicture(), 0, y, x, 50, null);

		g.setColor(Color.white);

		g.fillRect(x, y, 382, 50);

		g.setColor(new Color(39, 174, 96));

		g.drawRect(x, y, 382, 50);

		g.setFont(new Font("Tahoma", Font.PLAIN, 13));

		g.setColor(Color.black);

		g.drawString(s.getSwimmerName(), x + 10, y + 20);

		g.drawString(toString(), x + 10, y + 40);

		g.setFont(new Font("Tahoma", Font.PLAIN, 18));

		g.drawString(getTime(), x + 250, y + 30);
	}
}
