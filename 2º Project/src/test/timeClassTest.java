package test;

import engine.Contest.Partial;
import org.junit.Test;

import static org.junit.Assert.*;

public class timeClassTest
{

	@Test
	public void testTimeComparison()
	{
		Partial first = null;
		Partial second = null;
		Partial third = new Partial(0.1);
		Partial fourth = null;
		
		try {
			first = new Partial(54.001);
			second = new Partial(61.256);
			fourth = new Partial("01", "54", "123");
			third.setContestTime(2.542);	
			third.setContestTime(-02.32);	
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
		assertEquals("Time value shouldn't change!", third.compareTo(new Partial(2.542)), 0);
		
		assertTrue("First is better time than second!", first.isBetterTimeThan(second));
		
		assertFalse("Second is not better than first!", second.isBetterTimeThan(first));
		
		assertTrue("First is better time than fourth!", first.isBetterTimeThan(fourth));
	}
	
	@Test
	public void testTimeConversion()
	{
		Partial first = null;
		Partial second = null;
		Partial third = null;
		Partial forth = null;
		Partial fifth = null;
		Partial sixth = null;
		Partial seventh = null;
		
		try {
			first = new Partial(54.001);
			second = new Partial(61.256);
			third = new Partial(61.909);
			forth = new Partial("01:23.123");
			fifth = new Partial(09.999);
			sixth = new Partial(00.999);
			seventh = new Partial("1", "45", "145");
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
		
		assertEquals("Conversion is wrong", first.toString(), "00:54.001");
		assertEquals("Conversion is wrong", second.toString(), "01:01.256");
		assertEquals("Conversion is wrong", third.toString(), "01:01.909");
		assertEquals("Conversion is wrong", forth.toString(), "01:23.123");
		assertEquals("Conversion is wrong", fifth.toString(), "00:09.999");
		assertEquals("Conversion is wrong", sixth.toString(), "00:00.999");
		assertEquals("Conversion is wrong", seventh.toString(), "01:45.145");
	}
}
