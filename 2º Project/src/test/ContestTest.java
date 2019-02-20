package test;

import engine.Contest.Contest;
import engine.Contest.ContestOptions;
import engine.Contest.Partial;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ContestTest {

    @Test
    public void testContestCreation() {
        Partial first = new Partial("00:15.000");
        Partial second = new Partial("0", "13", "546");
        Partial third = new Partial(16.987);
        Partial fourth = new Partial(10.989);
        ArrayList<Partial> partials = new ArrayList<Partial>();

        partials.add(first);
        partials.add(second);
        partials.add(third);
        partials.add(fourth);

        Contest c = new Contest(1, partials, new Date(), ContestOptions.SwimmingStyle.FREESTYLE, ContestOptions.poolDimensions.SHORT);

        assertEquals("Coversion is not correct!", "00:56.522", c.getTime());
        assertEquals("Time is incorrect!", 56.522, c.getConstestTime(), 0.001);
        assertEquals("Contest length is wrong!", 100, c.getContestDistance());

        c = new Contest(2, partials, new Date(), ContestOptions.SwimmingStyle.FREESTYLE, ContestOptions.poolDimensions.LONG);

        assertEquals("Contest length is wrong!", 200, c.getContestDistance());
    }
}
