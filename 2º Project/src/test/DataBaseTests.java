package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Contest.Partial;
import engine.Swimmer.Swimmer;
import engine.db.DataBaseController;

public class DataBaseTests {
	
	DataBaseController q = null;
	
	ResultSet set = null;
	
	@Test(timeout=5000)
	public void testSimpleQuerie() {

		q = new DataBaseController("jdbc:sqlite:./database/app.db");
		
		ArrayList<Swimmer> querieResultSwimmers = q.getSwimmers("cam", true);
		
		assertTrue("databse is empty!", querieResultSwimmers.size() > 0);
		
		for(Swimmer s : querieResultSwimmers) {
			System.out.println(s.getSwimmerName() + " nasceu " + s.getSwimmerDateOfBirth());
		}
		
		q = null;
		
		set = null;
	}
	
	@Test(timeout=5000)
	public void testInsertSwimmerIntoDatabase() {
		
		q = new DataBaseController("jdbc:sqlite:./database/app.db");
		
		int initialNumberOfSwimmers = q.getSwimmers("cam", true).size();
		
		q.insertSwimmer("test", "2018-05-12", "test.jpg", 1, 1);
		
		int finalNumberOfSwimmers = q.getSwimmers("cam", true).size();
		
		q = null;
		
		set = null;
		
		assertTrue("Insert failed!" , finalNumberOfSwimmers > initialNumberOfSwimmers);
	}
	
	@Test(timeout=5000)
	public void testDeleteSwimmerFromDatabase() {
		
		q = new DataBaseController("jdbc:sqlite:./database/app.db");
		
		ArrayList<Swimmer> swimmers = q.getSwimmers("cam", true);
		
		int initialNumberOfSwimmers = swimmers.size();
		
		for(Swimmer s : swimmers) {
			if(s.getSwimmerName().equals("test")) {
				q.deleteSwimmer(s.getID());
			}
		}
		
		int finalNumberOfSwimmers = q.getSwimmers("cam", true).size();
		
		assertTrue("Delete failed!" , finalNumberOfSwimmers < initialNumberOfSwimmers);
		
		q = null;
		
		set = null;
	}
	
	@Test(timeout=5000) 
	public void testUserCreation () {
		
		String username = "test";
		String password = "test";
		
		q = new DataBaseController("jdbc:sqlite:./database/app.db");
		
		assertFalse("User exists!", q.checkIfUserExists(username));
		
		q.insertNewUser(username, password, "test", q.getPoolNames().get(0), false);
		
		assertTrue("Username doesn t exist!", q.checkIfUserExists(username));
		
		assertTrue("Passwords dont match!", password.equals(q.getStoredPassword(username)));
		
		assertTrue("User couldn't be retrieved", q.getUser(username) != null);
		
		q = null;
		
		set = null;
	}
	
	@Test(timeout=5000) 
	public void testAddContestToSwimmer() {
		q = new DataBaseController("jdbc:sqlite:./database/app.db");
		
		q.insertSwimmer("test", "2018-03-03", "test.jpg", 1, 1);
		
		int testSwimmerID = -1;
		int updatedNumberOfContests = -1;	
		int previousNumberOfContests = -1;

		ArrayList<Swimmer> swimmers = q.getSwimmersWithContests("cam", true);
		
		for(Swimmer sw : swimmers) {
			if(sw.getSwimmerName().equals("test")) {
				testSwimmerID = sw.getID();
				previousNumberOfContests = sw.getNumberOfContests();
			}
		}
		
		assertTrue("Swimmer costests should be 0", previousNumberOfContests == 0);
		
		Partial first = new Partial("00:15.000");
		Partial second = new Partial("00:16.000");
		Partial third = new Partial("00:17.000");
		
		ArrayList<Partial> firstContest = new ArrayList<Partial>();
		ArrayList<Partial> secondContest = new ArrayList<Partial>();
		ArrayList<Partial> thirdContest = new ArrayList<Partial>();
		
		for(int i = 0; i < 4; i++) {
			firstContest.add(first.clone());
			secondContest.add(second.clone());
			thirdContest.add(third.clone());
		}
		
		Contest f = new Contest(1, firstContest, new Date(), ContestOptions.SwimmingStyle.FREESTYLE, ContestOptions.poolDimensions.SHORT);
		Contest s = new Contest(2, secondContest, new Date(), ContestOptions.SwimmingStyle.BACKSTROKE, ContestOptions.poolDimensions.SHORT);
		Contest t = new Contest(3, thirdContest, new Date(), ContestOptions.SwimmingStyle.BUTTERFLY, ContestOptions.poolDimensions.SHORT);
		
		q.addContestToSwimmer(f, testSwimmerID);
		q.addContestToSwimmer(s, testSwimmerID);
		q.addContestToSwimmer(t, testSwimmerID);
		
		swimmers = q.getSwimmersWithContests("cam", true);
		
		for(Swimmer sw : swimmers) {
			if(sw.getSwimmerName().equals("test")) {
				updatedNumberOfContests = sw.getNumberOfContests();
			}
		}
		
		assertTrue("Número de tempos devia ser maior!", updatedNumberOfContests > previousNumberOfContests);
		assertEquals("Número de tempos devia ser 3!", updatedNumberOfContests, 3);
		assertTrue("Devia existir tempo", q.getContestFromSwimmer(testSwimmerID, "Freestyle" , 25, 100) != null);
		assertTrue("Devia existir tempo", q.getContestFromSwimmer(testSwimmerID, "Backstrocke" , 25, 100) != null);
		assertTrue("Devia existir tempo", q.getContestFromSwimmer(testSwimmerID, "Butterfly" , 25, 100) != null);
		
		q.deleteSwimmer(testSwimmerID);
		
		q = null;
		
		set = null;
	}

}
