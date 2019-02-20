package engine;

/**
 * Class that holds info of user necessary for database interaction
 */
public class Coach {
	int id;
	String name;
	String userName;
	int pool_id;
	boolean poolMaster;

	/**
	 * Constructor for Coach class
	 * @param id - coach id in database
	 * @param name - coach name
	 * @param username - coach username to login
	 * @param pool_id - pool id that coach belongs to
	 * @param master - is coach a pool master or not
	 */
	public Coach(int id, String name, String username, int pool_id, boolean master) {
		this.id = id;
		this.name = name;
		this.userName = username;
		this.pool_id = pool_id;
		poolMaster = master;
	}

	/**
	 * @return coach ID.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return Coach name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Coach username.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @return coach pool ID that he belongs to.
	 */
	public int getPool_id() {
		return pool_id;
	}
	
	/**
	 * @return true if coach is pool master, false otherwise.
	 */
	public boolean isPoolMaster() {
		return poolMaster;
	}
}
