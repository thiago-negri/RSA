package rsa.primality.test;

import java.math.BigInteger;

import static org.junit.Assert.*;

import org.junit.Test;

import rsa.primality.MillerRabin;


public class MillerRabinTest {

	@Test
	public void falsePrimalityTest() {
		MillerRabin mr = new MillerRabin();
		
		assertFalse("221 é primo?", mr.test(BigInteger.valueOf(221)) );
		assertFalse("341 é primo?", mr.test(BigInteger.valueOf(341)) );
		
				
	}
	
	
	@Test
	public void realPrimalityTest() {
		MillerRabin mr = new MillerRabin();
		//http://www.bigprimes.net/archive/prime
		assertTrue("80737 é primo?", mr.test(BigInteger.valueOf(80737)) );
		assertTrue("1020379 é primo?", mr.test(BigInteger.valueOf(1020379)) );
		

		
		
				
	}
	
}
