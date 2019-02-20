package test;

import engine.Contest.ContestTime;
import engine.Contest.Partial;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ContestTimeTest {

    @Test
    public void testSimpleContestTimeCreation() {
        Partial first = new Partial("00:13.323");

        ArrayList<Partial> partials = new ArrayList<Partial>();

        partials.add(first);

        ContestTime total = new ContestTime(partials);

        assertEquals("Total time is wrong!", "00:13.323" , total.toString());
    }

    @Test
    public void testComplexContestTimeCreation() {
        Partial first = new Partial("00:15.000");

        ArrayList<Partial> partials = new ArrayList<Partial>();

        for(int i = 0; i < 4; i++) {
            partials.add(first.clone());
        }

        ContestTime total = null;

        try {
            total = new ContestTime(partials);
        }
        catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        assertTrue("The total time is miss calculated!", total.getConstestTime() == 60.0d);

        assertEquals("Time representation is wrong!", "01:00.000", total.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContestTimeThrow() {
        Partial first = new Partial("00:15.000");

        ArrayList<Partial> partials = new ArrayList<Partial>();

        for(int i = 0; i < 3; i++) {
            partials.add(first.clone());
        }

        @SuppressWarnings("unused")
		ContestTime total = null;

        total = new ContestTime(partials);
    }

    @Test
    public void testContestTimegettersAndSetters() {
        Partial first = new Partial("00:15.000");
        Partial second = new Partial("0", "13", "546");
        Partial third = new Partial(16.987);
        Partial fourth = new Partial(10.989);

        ArrayList<Partial> partials = new ArrayList<Partial>();

        partials.add(first);
        partials.add(second);
        partials.add(third);
        partials.add(fourth);

        ContestTime contest = new ContestTime(partials);

        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(0).compareTo(first));
        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(1).compareTo(second));
        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(2).compareTo(third));
        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(3).compareTo(fourth));
        assertEquals("Coversion is not correct!", "00:56.522", contest.toString());
        assertEquals("Time is incorrect!", 56.522, contest.getConstestTime(), 0.001);

        contest.setPartial(new Partial("00:13.000"), 0);
        contest.setPartial(new Partial("00:12.000"), 1);
        contest.setPartial(new Partial("00:13.123"), 2);
        contest.setPartial(new Partial("00:15.230"), 3);

        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(0).compareTo(new Partial("00:13.000")));
        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(1).compareTo(new Partial("00:12.000")));
        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(2).compareTo(new Partial("00:13.123")));
        assertEquals("Didn't get the correect partial!", 0, contest.getPartial(3).compareTo(new Partial("00:15.230")));
    }
}
