package rsa.primality;

import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.*;

public class EuclideanFinderTest {
	
	@Test
	public void inverse() {
		EuclideanFinder eFinder = new EuclideanFinder();
		//assertEquals(BigInteger.valueOf(2471), eFinder.findMultiplicativeInverseOf(BigInteger.valueOf(11111), BigInteger.valueOf(12345)));
		assertEquals(BigInteger.valueOf(113), eFinder.findMultiplicativeInverseOf(BigInteger.valueOf(17), BigInteger.valueOf(120)));
	}
	
	@Test
	public void gcdTest() {
		EuclideanFinder euclid = new EuclideanFinder();
		assertEquals(BigInteger.valueOf(21), euclid.greatestCommonDivisor(BigInteger.valueOf(252), BigInteger.valueOf(105)));
		
	}
	
	@Test
	public void findRelativePrime() {
		//http://cryptoclub.math.uic.edu/mathfunctions/rel_prime.html
		EuclideanFinder euclid = new EuclideanFinder();
		assertEquals(BigInteger.valueOf(3), euclid.findRelativePrimeOf(new BigInteger("18014398241046527")));
		assertEquals(BigInteger.valueOf(3), euclid.findRelativePrimeOf(new BigInteger("1125899839733759")));
		assertEquals(BigInteger.valueOf(5), euclid.findRelativePrimeOf(new BigInteger("11258998397337")));
		assertEquals(BigInteger.valueOf(5), euclid.findRelativePrimeOf(new BigInteger("57")));
		
		
	}

}
