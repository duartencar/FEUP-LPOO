package test;

import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import engine.Swimmer.Swimmer;
import engine.db.DataBaseController;

public class DataBaseTests {

	@Test(timeout=500)
	public void test() {
		DataBaseController db = null;
		
		try {
			db = new DataBaseController("jdbc:sqlite:./database/app.db");
		} catch(IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
		assertTrue("Connection not established", db != null);
	}
	
	@Test(timeout=5000)
	public void testSimpleQuerie() {
		DataBaseController q = null;
		
		ResultSet set = null;
		
		q = new DataBaseController("jdbc:sqlite:./database/app.db");
		
		ArrayList<Swimmer> querieResultSwimmers = q.getSwimmers("cam", true);
		
		assertTrue("databse is empty!", querieResultSwimmers.size() > 0);
		
		for(Swimmer s : querieResultSwimmers) {
			System.out.println(s.getSwimmerName() + " nasceu " + s.getSwimmerDateOfBirth());
		}
	}

}
