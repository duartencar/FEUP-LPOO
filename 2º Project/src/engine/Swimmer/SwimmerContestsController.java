package engine.Swimmer;

import java.util.ArrayList;

import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Contest.ContestOptions.SwimmingStyle;

/**
 * Class that holds contest controllers
 * @see Contest
 * @see ContestOptions
 */
public class SwimmerContestsController {

	private ArrayList<TypeOfContestController> swimmerContests;

	/**
	 * Default constructor of TypeOfContestController CLass
	 */
	public SwimmerContestsController() {

		swimmerContests = new ArrayList<TypeOfContestController>();
	}

	/**
	 * Adds a new contest. If TypeOfContestController with a style equal to the new contest already exists adds it to that controler, if it doesn't it creates a new one.
	 * @param newContest - contest to be added
	 */
	public void add(Contest newContest) {

		if(newContest == null) {
			throw new IllegalArgumentException("Contest you are trying to add is null!");
		}

		for(TypeOfContestController controller : swimmerContests) {

			if(controller.getControllerStyle() == newContest.getStyle()){
				controller.add(newContest);
				return;
			}
		}

		TypeOfContestController newController = new TypeOfContestController(newContest.getStyle());

		newController.add(newContest);

		swimmerContests.add(newController);
	}

	/**
	 * @return number of contests that a swimmer has
	 */
	public int getNumberOfContests() {
		int n = 0;

		for(TypeOfContestController controller: swimmerContests) {
			n += controller.length();
		}

		return n;
	}

	/**
	 * Gets the best contest of a swimmer at a certain style.
	 * @param style - the style of contest
	 * @return best contest of a style
	 */
	public Contest getBestContestAt(SwimmingStyle style) {

		for(TypeOfContestController type : swimmerContests) {
			if(type.getControllerStyle() == style) {
				return type.getBest();
			}
		}

		return null;

	}

	/**
	 * Gets all the contests of a swimmer at a certain style.
	 * @param style - the style of contest
	 * @return a list containg all the contests of the given style of that swimmer.
	 */
	public ArrayList<Contest> getContestAt(ContestOptions.SwimmingStyle style) {

		for(TypeOfContestController type : swimmerContests) {

			if(type.getControllerStyle() == style) {
				return type.getAll();
			}
		}

		return null;
	}

	/**
	 * @return a list containg all the contests of a swimmer.
	 */
	public ArrayList<Contest> getAllContest() {
		ArrayList<Contest> contests = new ArrayList<Contest>();

		for(TypeOfContestController type : swimmerContests) {
			for(Contest c : type.getAll()) {
				contests.add(c);
			}
		}

		return contests;
	}
}
