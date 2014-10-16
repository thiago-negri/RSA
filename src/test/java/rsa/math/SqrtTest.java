package rsa.math;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import rsa.math.sqrt.KarraSqrt;
import rsa.math.sqrt.SqrtMethod;
import rsa.math.sqrt.WikiSqrt;

@Ignore
public class SqrtTest {

	@Test
	public void karraTest() {
		SqrtMethod karraMethod = new KarraSqrt();
		testSqrtMethod(karraMethod);
	}
	
	@Test
	public void wikiTest() {
		SqrtMethod karraMethod = new WikiSqrt();
		testSqrtMethod(karraMethod);
	}
	
	
	public void testSqrtMethod(SqrtMethod method) {
		int bits = 128;
		Random random = new Random();
		BigInteger n;
		BigInteger n2;
		BigDecimal sqrt;
		while (bits < 2048) {
			n = BigInteger.probablePrime(bits, random);
			n2 = n.multiply(n);
			sqrt = method.sqrt(n2);
			assertEquals(n, sqrt);
			System.out.println("n= " + n2 + "\nbits: " + n.bitLength() + "\nsqr= " + sqrt);
			bits += 64;
		}
		  
	}
	
}
