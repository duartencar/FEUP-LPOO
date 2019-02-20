package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Contest.Partial;
import engine.Swimmer.Swimmer;

public class swimmerTests {

	@Test
	public void testSwimmerCreation() {
		
		Swimmer x = new Swimmer(1, "Duarte", "1997-02-11");
		
		assertEquals("Swimmer name is wrong!", "Duarte", x.getSwimmerName());
		
		assertEquals("Swimmer birthday is wrong!", "1997-02-11", x.getSwimmerDateOfBirth());
		
		assertEquals("Swimmer hasn't got contests!", 0, x.getNumberOfContests());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testThrow() {
		
		String z = null;
		
		@SuppressWarnings("unused")
		Swimmer x = new Swimmer(1, "Duarte",  z);
	}
	
	@Test
	public void testGetBestSwimmerContests() {
		
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
		Contest s = new Contest(2, secondContest, new Date(), ContestOptions.SwimmingStyle.FREESTYLE, ContestOptions.poolDimensions.SHORT);
		Contest t = new Contest(3, thirdContest, new Date(), ContestOptions.SwimmingStyle.FREESTYLE, ContestOptions.poolDimensions.SHORT);
		
		Swimmer x = new Swimmer(1, "Duarte",  "1997-02-11");
		
		x.add(t);
		x.add(s);
		x.add(f);
		
		Contest best = x.getBestContestAt(ContestOptions.SwimmingStyle.FREESTYLE);
		
		assertEquals("This is not the best contest!", f.clone().getConstestTime(), best.getConstestTime() , 0.00001);
		assertEquals("This is not the best time representation!", f.clone().getTime(), best.getTime());
	}
	
	@Test
	public void testGettingSwimmerContests() {
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
		Contest s = new Contest(2, secondContest, new Date(), ContestOptions.SwimmingStyle.FREESTYLE, ContestOptions.poolDimensions.SHORT);
		Contest t = new Contest(3, thirdContest, new Date(), ContestOptions.SwimmingStyle.FREESTYLE, ContestOptions.poolDimensions.SHORT);
		
		Swimmer x = new Swimmer(1, "Duarte",  "1997-02-11");
		
		x.add(t);
		x.add(s);
		x.add(f);
		
		ArrayList<Contest> swimmerContests = x.getContestAt(ContestOptions.SwimmingStyle.FREESTYLE);
		
		assertEquals("The number of swimmer contests is wrong!", 3, swimmerContests.size());
	}

}
