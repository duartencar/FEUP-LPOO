package engine.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.common.annotations.VisibleForTesting;

import engine.Coach;
import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Contest.Partial;
import engine.Swimmer.Swimmer;

/**
 * Class that interacts with SQLite3 database , retrieving data and inserting new data
 * @see ContestTime
 * @see Partial
 */
public class DataBaseController {

	Connection c = null;
	Statement stm = null;
	String path = null;

	/**
	 * Constructor of DataBaseController CLass
	 * @param path - relative path to database file
	 */
	public DataBaseController(String path) {
		this.path = path;
	}

	/**
	 * Establishes connection to the database and gets all rows that result from a give querie
	 * @param querie - querie to be executed in database
	 * @throws ClassNotFoundException - if jdbc jar is not found
	 * @throws SQLException - if querie fails
	 * @return all rows that resulted from the queried
	 */
	private ResultSet makeSimpleQuerie(String querie) throws ClassNotFoundException, SQLException {

		establishConnection();

		return stm.executeQuery(querie);
	}

	/**
	 * Gets JDBC jar and establishes connection to the database
	 * @throws ClassNotFoundException - if jdbc jar is not found
	 * @throws SQLException - if querie fails
	 */
	private void establishConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");

		c = DriverManager.getConnection(path);

		c.setAutoCommit(false);

		System.out.println("Connected to: " + path);

		stm = c.createStatement();
	}

	/**
	 * Executes an INSERT or an UPDATE sqlite3 command
	 * @param cmd - command to be executed
	 * @throws SQLException if querie fails
	 */
	private void executeCommand(String cmd) throws SQLException {
		stm.executeUpdate(cmd);

		c.commit();
	}

	/**
	 * Cuts connection to the databse so other connections can be established
	 * @throws SQLException - if querie fails
	 */
	private void breackConnection(ResultSet x) throws SQLException {
		if(x != null) {
			x.close();
		}

		stm.close();

		c.close();

		System.out.println("Connection breaked");
	}

	/**
	 * Executes an INSERT or an UPDATE operation
	 * @param cmd - INSERT or UPDATE command
	 * @return true if operation is successful
	 */
	private boolean insert(String cmd) {
		try {
			establishConnection();

			executeCommand(cmd);;

			breackConnection(null);
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println("INSERT ERROR -> " + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Gets coach swimmers from database.
	 * @param username - String containing coach userName
	 * @param poolMaster - boolean value indicating if coach is a pool master or not
	 * @return an ArrayList of Swimmers. If user is a pool master, all swimmers from the pool that the user belong will be loaded
	 */
	public ArrayList<Swimmer> getSwimmers(String username, boolean poolMaster) {

		ArrayList<Swimmer> coachSwimmers = new ArrayList<Swimmer>();

		Swimmer aux = null;

		ResultSet querieResults = null;

		String querie = null;

		if(poolMaster) {
			querie = String.format("SELECT DISTINCT * FROM SWIMMER WHERE POOL_ID=(SELECT POOL_ID FROM COACH WHERE USERNAME='%s');", username);
		}
		else {
			querie = String.format("SELECT DISTINCT * FROM SWIMMER WHERE COACH_ID=(SELECT ID FROM COACH WHERE USERNAME='%s');", username);
		}

		try {
			querieResults = makeSimpleQuerie(querie);

			while(querieResults.next()) {
				int id = querieResults.getInt("id");
				String nome = querieResults.getString("name");
				String dob = querieResults.getString("dob");
				int coach_id = querieResults.getInt("coach_id");
				int pool_id = querieResults.getInt("pool_id");
				String imageName = querieResults.getString("profil_pic");

				aux = new Swimmer(id, nome, dob);
				aux.setID(id);
				aux.setCoach_id(coach_id);
				aux.setPool_id(pool_id);
				aux.loadProfilePicture(imageName);
				coachSwimmers.add(aux);
			}

			breackConnection(querieResults);
		}
		catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		return coachSwimmers;
	}

	/**
	 * Gets password od user from database
	 * @param username - String containing coach userName
	 * @return a String representing password hash
	 */
	public String getStoredPassword(String username) {

		String querie = String.format("select password from coach where username='%s';", username);

		ResultSet querieResults = null;

		String rs = null;

		try {
			querieResults = makeSimpleQuerie(querie);

			if(querieResults.next()) {
				rs = querieResults.getString("password");
			}
			else {
				rs = null;
			}

			breackConnection(querieResults);
		}
		catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		return rs;
	}

	/**
	 * Checks if username exists in database
	 * @param username - userName to check
	 * @return true if username exists in the database, false otherwise
	 */
	public boolean checkIfUserExists(String userName) {
		String querie = String.format("select username from coach where username='%s';", userName);

		ResultSet querieResults = null;

		try {
			querieResults = makeSimpleQuerie(querie);

			if(querieResults.next()) {
				breackConnection(querieResults);
				return true;
			}

			breackConnection(querieResults);
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	/**
	 * @return all the pool names that exist in database
	 */
	public ArrayList<String> getPoolNames() {

		ArrayList<String> poolNames = new ArrayList<String>();

		String querie = "SELECT NAME FROM POOL;";

		ResultSet querieResults = null;

		try {
			querieResults = makeSimpleQuerie(querie);

			while(querieResults.next()) {
				poolNames.add(querieResults.getString("Name"));
			}

			breackConnection(querieResults);
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}

		return poolNames;
	}

	/**
	 * Inserts new user into database
	 * @param username - username of the new swimmer
	 * @param hashedPassword - password of the new swimmer
	 * @param name - name of the new swimmer
	 * @param poolName - name of the pool that the new swimmer belongs to
	 * @param poolMaster - boolean value signaling if user is a pool master or not
	 * @return true if user is added with success, false otherwise
	 */
	public boolean insertNewUser(String username, String hashedPassword, String name, String poolName, boolean poolMaster) {

		String querie = String.format("SELECT ID FROM POOL WHERE NAME='%s';", poolName);

		int poolID = -1;

		ResultSet querieResults = null;

		try {
			querieResults = makeSimpleQuerie(querie);

			if(querieResults.next()) {
				poolID = querieResults.getInt("ID");
			}

			breackConnection(querieResults);
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

		if(poolID == -1) {
			return false;
		}

		querie = String.format("INSERT INTO COACH (USERNAME, PASSWORD, NAME, POOL_ID, POOLMASTER) VALUES('%s', '%s', '%s', %d, %d);", username, hashedPassword, name, poolID, poolMaster ? 1 : 0);


		try {
			if(!insert(querie)) {
				breackConnection(null);
				return false;
			}

			//breackConnection(null);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * gets user with a certain username
	 * @param username - username of the coach
	 * @return Coach class with all the info
	 */
	public Coach getUser(String username) {

		String querie = String.format("select id, username, name, pool_id, poolmaster from coach where username='%s';", username);

		ResultSet querieResults = null;

		Coach newCoach = null;

		try {
			querieResults = makeSimpleQuerie(querie);

			if(querieResults.next()) {
				newCoach = new Coach(
						querieResults.getInt("id"),
						querieResults.getString("name"),
						username,
						querieResults.getInt("pool_id"),
						querieResults.getInt("poolmaster") == 1 ? true : false);
			}
			else {
				newCoach = null;
			}

			breackConnection(querieResults);
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}

		return newCoach;
	}

	/**
	 * Inserts new swimmer into database
	 * @param swimmerName - swimmer name
	 * @param dob - swimmer birthday
	 * @param pictureName - profile picture name
	 * @param coach_id - id of the coach that the new swimmer bellongs to
	 * @param pool_id - id of the pool that the new swimmer bellongs to
	 * @return true if swimmer is added with success, false otherwise
	 */
	public boolean insertSwimmer(String swimmerName, String dob, String pictureName, int coach_id, int pool_id) {

		String querie = String.format("INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID, PROFIL_PIC) VALUES('%s', '%s', %d, %d, '%s');", swimmerName,dob, coach_id, pool_id, pictureName);

		return insert(querie);
	}

	/**
	 * Deletes swimmer from database
	 * @param swimmerID - swimmer id to be removed
	 * @return true if swimmer was deleted with success, false otherwise
	 */
	public boolean deleteSwimmer(int swimmerID) {
		String querie = String.format("DELETE FROM SWIMMER WHERE ID=%d;", swimmerID);

		return insert(querie);
	}

	/**
	 * Gets a representation of a given date
	 * @param x - date to extract string
	 * @return String with date representation in fromat YYYY-MM-DD
	 */
	@SuppressWarnings("deprecation")
	public String extractDate(Date x) {
		return String.format("%d-%02d-%02d", x.getYear() + 1900, x.getMonth()+1, x.getDate());
	}

	/**
	 * Fills a given Contest with his partials
	 * @param x - contest to be filled
	 * @return true if contests was filled successfully
	 */
	private boolean getPartialsThatBelogToContest(Contest x) {

		String querie = String.format("SELECT * FROM PARTIAL WHERE CONTEST_ID=%d;", x.getId());

		ResultSet querieResults = null;

		try {
			querieResults = makeSimpleQuerie(querie);

			while(querieResults.next()) {
				double t = querieResults.getFloat("PARTIAL_TIME");

				x.addPartial(new Partial(t));
			}
			breackConnection(querieResults);
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("ERROR QUERING PARTIALS IN ID= " + x.getId() +  " -> " + e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * Extracts a Date class from String
	 * @param d - String date represatation
	 * @return Date class extracted from given String
	 */
	public Date extractDateFromString(String d) {

		if(d == null) {
			return null;
		}

		Date cDate = new Date();

		String[] dateElements = d.split("-");

		cDate.setYear(Integer.parseInt(dateElements[0]) - 1900);
		cDate.setMonth(Integer.parseInt(dateElements[1]) - 1);
		cDate.setDate(Integer.parseInt(dateElements[2]));

		return cDate;
	}

	/**
	 * Addas a contest to a swimmer
	 * @param x - Contest to be added
	 * @param swimmerID - swimmer ID that contest info will be added
	 * @return true if contest was added successfully
	 */
	public boolean addContestToSwimmer(Contest x, int swimmerID) {
		String cmd = String.format("INSERT INTO CONTEST (STYLE, DAYOFCONTEST, POOLLENGTH, SWIMER_ID) VALUES('%s', '%s', %d, %d);", x.getStyle().getTypeName(), extractDate(x.getDate()), x.getIntPoolLength(), swimmerID);

		if(!insert(cmd)) {
			System.out.println("Contest insert failed");
			return false;
		}

		Partial p = null;

		ResultSet querieResults = null;

		int insertedContestID = -1;

		try {
			querieResults = makeSimpleQuerie("SELECT MAX(ID) FROM CONTEST;");

			if(querieResults.next()) {
				insertedContestID = querieResults.getInt(1);
			}

			breackConnection(querieResults);
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println("QUERING MAX ID FAILED -> " + e.getMessage());
			return false;
		}

		if(insertedContestID < 0) {
			System.out.println("FAILED Contest ID retrieved = " + insertedContestID);
			return false;
		}

		for(int i = 0; i < x.getNumberOfPartials(); i++) {
			p = x.getPartial(i);

			if(p == null) {
				return false;
			}

			cmd = String.format(Locale.US, "INSERT INTO PARTIAL (CONTEST_ID, PARTIAL_TIME) VALUES(%d, %.3f);", insertedContestID, p.getConstestTime());

			if(!insert(cmd)) {
				System.out.println("Failed to insert partial " + i);

				return false;
			}
		}

		return true;
	}

	/**
	 * Gets the best contest with given contest characteristics of a swimmer
	 * @param swimmerID - swimmer ID to get contest from
	 * @param style - contest style type
	 * @param poolLength - contest pool type
	 * @param contestDistance - contest distance
	 * @return the best contest with given characteristicas
	 */
	public Contest getContestFromSwimmer(int swimmerID, String style, int poolLength, int contestDistance) {
		Contest toReturn = null;

		String querie = String.format("SELECT * FROM CONTEST WHERE SWIMER_ID=%d AND POOLLENGTH=%d AND STYLE='%s';", swimmerID, poolLength, style);

		ResultSet querieResults = null;

		Contest queriedContest = null;

		ContestOptions.SwimmingStyle contestStyle = ContestOptions.SwimmingStyle.FREESTYLE.getSwimmingStyle(style);

		ContestOptions.poolDimensions contestPoolDImension =  poolLength == 25 ? ContestOptions.poolDimensions.SHORT : ContestOptions.poolDimensions.LONG;

		ArrayList<Contest> queriedContests = new ArrayList<Contest>();

		ArrayList<Contest> validContests = new ArrayList<Contest>();

		try {
			querieResults = makeSimpleQuerie(querie);

			while(querieResults.next()) {
				int id = querieResults.getInt("ID");
				String cDate = querieResults.getString("DAYOFCONTEST");
				queriedContest = new Contest(id, new ArrayList<Partial>(), extractDateFromString(cDate), contestStyle, contestPoolDImension);
				queriedContests.add(queriedContest);
			}

			breackConnection(querieResults);
		} catch (SQLException | ClassNotFoundException e) {
			try {
				breackConnection(querieResults);
			} catch (SQLException e1) {
				System.out.println(e1.getMessage());
			}
			System.out.println("ERROR QUERING CONTESTS -> " + e.getMessage());
		}

		if(queriedContests.size() == 0) {
			System.out.println("There are no older records for this type of contest!");
			return toReturn;
		}

		for(Contest x : queriedContests) {
			if(getPartialsThatBelogToContest(x)) {
				if(x.getContestDistance() == contestDistance) {
					validContests.add(x);
				}
			}
		}

		if(validContests.size() == 0) {
			System.out.println("No " + contestDistance + " m contests were found!");
			return toReturn;
		}

		toReturn = validContests.get(0);

		for(int i = 1; i < validContests.size(); i++) {
			if(validContests.get(i).isBetterTimeThan(toReturn)) {
				toReturn = validContests.get(i);
			}
		}

		return toReturn;
	}

	/**
	 * Gets all the swimmers from a coach with contests that belong to those swimmers
	 * @param username - coach username
	 * @param poolMaster - boolean value signaling if it is pool master or not
	 * @return an ArrayList containing the swimmers and their contests
	 */
	public ArrayList<Swimmer> getSwimmersWithContests(String username, boolean poolMaster) {
		ArrayList<Swimmer> swimmers = null;
		String cmd = null;
		ResultSet querieResults = null;


		if((swimmers = getSwimmers(username, poolMaster)) == null) {
			return null;
		}

		for(Swimmer x : swimmers) {

			cmd = String.format("SELECT * FROM CONTEST WHERE SWIMER_ID=%d;", x.getID());

			int id = -1;
			Date cDate = null;
			ContestOptions.SwimmingStyle style = null;
			ContestOptions.poolDimensions contestPoolDImension = null;
			Contest contextAux = null;
			ArrayList<Contest> swimmerXContests = new ArrayList<Contest>();

			try {
				querieResults = makeSimpleQuerie(cmd);

				while(querieResults.next()) {
					id = querieResults.getInt("ID");

					cDate = extractDateFromString(querieResults.getString("DAYOFCONTEST"));

					style = ContestOptions.SwimmingStyle.BUTTERFLY.getSwimmingStyle(querieResults.getString("STYLE"));

					contestPoolDImension =  (querieResults.getInt("POOLLENGTH") == 25 ? ContestOptions.poolDimensions.SHORT : ContestOptions.poolDimensions.LONG);

					contextAux = new Contest(id, new ArrayList<Partial>(), cDate, style, contestPoolDImension);

					swimmerXContests.add(contextAux);
				}

				breackConnection(querieResults);
			}
			catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

			if(swimmerXContests.size() == 0) {
				System.out.println(x.getSwimmerName() + " doesn't have contests!");
				continue;
			}

			for(Contest c : swimmerXContests) {
				if(getPartialsThatBelogToContest(c)) {
					x.add(c);
				}
			}

			System.out.println(x.getSwimmerName() + " has " + x.getNumberOfContests());
		}

		return swimmers;
	}
}
