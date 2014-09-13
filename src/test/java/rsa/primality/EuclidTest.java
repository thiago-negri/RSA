package rsa.primality;

import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.*;

public class EuclidTest {
	
	@Test
	public void gcdTest() {
		Euclid euclid = new Euclid();
		assertEquals(BigInteger.valueOf(21), euclid.greatestCommonDivisor(BigInteger.valueOf(252), BigInteger.valueOf(105)));
		
	}
	
	@Test
	public void findRelativePrime() {
		//http://cryptoclub.math.uic.edu/mathfunctions/rel_prime.html
		Euclid euclid = new Euclid();
		assertEquals(BigInteger.valueOf(3), euclid.findRelativePrimeOf(new BigInteger("18014398241046527")));
		assertEquals(BigInteger.valueOf(3), euclid.findRelativePrimeOf(new BigInteger("1125899839733759")));
		assertEquals(BigInteger.valueOf(5), euclid.findRelativePrimeOf(new BigInteger("11258998397337")));
		assertEquals(BigInteger.valueOf(5), euclid.findRelativePrimeOf(new BigInteger("57")));
		
		
	}

}
