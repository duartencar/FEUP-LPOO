package engine.Contest;

import java.util.ArrayList;

/**
 * Class that stores contests partials, and holds contest total duration
 * @see Partial
 */
public class ContestTime extends Partial {

	ArrayList<Partial> contestPartials;

	/**
	* constructor of ContestTime CLass
	* @param partials - ArrayList of Partials contains contests various partials
	* @throws  IllegalArgumentException if given partials if the number of partials is not a power of two
	*/
	public ContestTime(ArrayList<Partial> partials) throws IllegalArgumentException {

		if(!isPowerOfTwo(partials.size())) {
			throw new IllegalArgumentException("Number of partials is not a power of two!");
		}

		for(Partial x : partials) {
			time += x.getConstestTime();
		}

		contestPartials = partials;
	}

	/**
	* default constructor of ContestTime CLass
	*/
	public ContestTime() {
		contestPartials = null;

		time = 0;
	}

	/**
	* @return contest number of partials
	*/
	public int getNumberOfPartials() {
		return contestPartials.size();
	}

	/**
	* Check if number of partials is a power of two
	* @return true if the number of contests is a power of two
	* @see https://stackoverflow.com/questions/19383248/find-if-a-number-is-a-power-of-two-without-math-function-or-log-function
	*/
	boolean isPowerOfTwo(int size) {
		return ((size & (size - 1)) == 0);
	}

	/**
	* Get one of the contest partials
	* @param index - the partial index you want to get
	* @return Partial at the given index
	*/
	public Partial getPartial(int index) {
		if(index < 0  || index > contestPartials.size()) {
			return null;
		}

		return contestPartials.get(index);
	}

	/**
	* Adds a partial to contest
	* @param newPartial - partial to add
	*/
	public void addPartial(Partial newPartial) {
		contestPartials.add(newPartial);

		time += newPartial.getConstestTime();
	}

	/**
	* sets a partial to given index
	* @param newPartial - partial to add
	* @param index - index where to set partial
	* @return true if partial was succesfully set
	*/
	public boolean setPartial(Partial newPartial, int index) {
		if(index < 0  || index > contestPartials.size() || newPartial == null) {
			return false;
		}

		contestPartials.set(index, newPartial);

		return true;
	}
}
