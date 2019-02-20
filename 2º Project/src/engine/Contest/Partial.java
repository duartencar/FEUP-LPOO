package engine.Contest;


/**
 * Base class to store a time
 */
public class Partial implements Comparable
{
	protected double time = 0;

	/**
	* default constructor of Partial CLass
	*/
	public Partial () {
	}

	/**
	* constructor of Partial CLass
	* @param t  - time in seconds representing partial duration
	* @throws  IllegalArgumentException if given time is a negative number
	*/
	public Partial(double t) throws IllegalArgumentException {

		if(t < 0) {
			throw new IllegalArgumentException("The time must be greater than 0.");
		}

		this.time = t;
	}

	/**
	* constructor of Partial CLass
	* @param t  - string representation of the partial duration
	* @throws  IllegalArgumentException if given string doesn't respect format MM:ss.mmm
	*/
	public Partial(String t) throws IllegalArgumentException {

		if(t.length() != 9 || t.indexOf(':') < 0 || t.indexOf('.') < 0) {
			throw new IllegalArgumentException("Wrong time string format!");
		}

		this.time = getValueFromString(t);
	}

	/**
	* constructor of Partial CLass
	* @param min  - string representating number of minutes
	* @param sec  - string representating number of seconds
	* @param millis  - string representating number of miliseconds
	* @throws  IllegalArgumentException if one of the parameters converts to a negative number
	*/
	public Partial(String min, String sec, String millis) {
		double mins = 0;
		double secs = 0;
		double mils = 0;

		try {
			mins = Double.parseDouble(min);

			secs = Double.parseDouble(sec);

			mils = Double.parseDouble(millis);
		}
		catch(NumberFormatException e) {
			System.out.println("Failed to parse one of the parameters!");
		}
		catch(NullPointerException e) {
			System.out.println("One of the parameters is null!");
		}

		if((mins < 0 || mins > 99) || (secs < 0 || secs > 59) || (mils < 0 || mils > 999)) {
			throw new IllegalArgumentException("One of parameters ins negative!");
		}

		this.time = mins*60 + secs + mils / 1000;
	}

	/**
	* Extract duration from time formate string
	* @param t - string representing partial duration
	* @return real number representating partial duration
	*/
	private double getValueFromString(String t) {
		double value = 0;

		double stringValue;

		stringValue = Double.parseDouble(t.substring(0, 2));

		value += stringValue * 60;

		stringValue = Double.parseDouble(t.substring(3, 5));

		value += stringValue;

		stringValue = Double.parseDouble(t.substring(6, t.length()));

		value += stringValue / 1000;

		return value;
	}

	/**
	* @return the partial duration
	*/
	public double getConstestTime() {
		return this.time;
	}

	/**
	* Modifies partial duration
	* @param t - duration that partial now has
	* @throws IllegalArgumentException if given duration is less than 0
	*/
	public void setContestTime(double t) throws IllegalArgumentException {

		if(t < 0) {
			throw new IllegalArgumentException("The time must be greater than 0.");
		}

		this.time = t;
	}

	/**
	* Compares two Partials
	* @param otherTime - partial to compare to
	* @return true if this partial is better than given partial
	*/
	public boolean isBetterTimeThan(Partial otherTime) {

		return compareTo(otherTime) == 1;
	}

	/**
	* Compares to a partial
	* @param arg0 - partial to compare to
	* @return 1 if this partial is better than given partial, 0 if they are equal, -1 if it s worst
	*/
	@Override
	public int compareTo(Object arg0)
	{
		Partial compare = (Partial)arg0;

		if(this.getConstestTime() < compare.getConstestTime()) {
			return 1;
		}
		else if(this.getConstestTime() > compare.getConstestTime()) {
			return -1;
		}
		else {
			return 0;
		}
	}

	/**
	* Partial to string implementation
	* @return String representation of partial duration
	*/
	@Override
	public String toString()
	{
		int minutes = (int)time / 60;

		int seconds = (int)(time - minutes * 60);

		int millis = (int) Math.round((time - minutes * 60 - seconds)*1000);

		return  String.format("%02d:%02d.%03d", minutes, seconds, millis);
	}

	/**
	* Negative time partial
	* @return String representation of negative partial duration
	*/
	public String negativeToString() {
		int minutes = (int)time / 60;

		int seconds = (int)(time - minutes * 60);

		int millis = (int) Math.round((time - minutes * 60 - seconds)*1000);

		return  String.format("-%02d:%02d.%03d", minutes, seconds, millis);
	}

	/**
	* partial clone implementation
	* @return this partial clone
	*/
	@Override
	public Partial clone() {
		return new Partial(this.time);
	}
}
