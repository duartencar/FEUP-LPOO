package engine.Swimmer;

import engine.Contest.Contest;
import engine.Contest.ContestOptions;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Class that holds contests of a certain type of contest
 * @see Contest
 * @see ContestOptions
 */
public class TypeOfContestController {
	private ContestOptions.SwimmingStyle styleType;
	private TreeSet<Contest> styleContests;

	/**
	 * Constructor of TypeOfContestController CLass
	 * @param style - type of contest of this controller
	 */
	public TypeOfContestController(ContestOptions.SwimmingStyle style) {

		if(style == null) {
			throw new IllegalArgumentException();
		}

		styleType = style;

		styleContests = new TreeSet<Contest>();
	}

	/**
	 * Get controller style
	 * @return the type of contest of the controller
	 */
	public ContestOptions.SwimmingStyle getControllerStyle() {
		return styleType;
	}

	/**
	 * Adds a contest to this controller
	 * @param x - Contest to be added
	 */
	public void add(Contest x) {
		styleContests.add(x);
	}

	/**
	 * @return the number of contests
	 */
	public int length() {
		return styleContests.size();
	}

	/**
	 * @return the best contest (the fastest) of this controller
	 */
	public Contest getBest() {
		//so the contest is not lost
		Object x = styleContests.clone();

		return ((TreeSet<Contest>) x).pollLast();
	}

	/**
	 * @return a list of all the contests in this controller
	 */
	public ArrayList<Contest> getAll() {

		if(styleContests.size() == 0) {
			return null;
		}

		ArrayList<Contest> contests = new ArrayList<Contest>();

		for(Contest swimmerContest : styleContests) {
			contests.add(swimmerContest.clone());
		}

		return contests;
	}
}
