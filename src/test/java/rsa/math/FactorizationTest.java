package rsa.math;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import rsa.math.factorization.AmbdekarFactorization;
import rsa.math.factorization.FactorMethod;
import rsa.math.factorization.Factors;
import rsa.math.factorization.NaiveFactorization;
import rsa.math.sqrt.KarraSqrt;
import rsa.math.sqrt.SqrtMethod;

@Ignore
public class FactorizationTest {

	@Test
	public void ambdekarTest() {
		FactorMethod ambdekarMethod = new AmbdekarFactorization();
		ambdekarMethod.factor(new BigInteger("999962000357"), new KarraSqrt());
		ambdekarMethod.factor(new BigInteger("80964686104403"), new KarraSqrt());
		 
		testFactorizationMethod(ambdekarMethod, new KarraSqrt());
	}
	
	@Test
	public void naiveTest() {
		FactorMethod naiveFactor = new NaiveFactorization();
		testFactorizationMethod(naiveFactor, new KarraSqrt());
	}
	
	
	public void testFactorizationMethod(FactorMethod factorMethod, SqrtMethod sqrtMethod) {
		int bits = 16;
		Random random = new Random();
		BigInteger p;
		BigInteger q;
		BigInteger n;
		while (bits < 2048) {
			p  = BigInteger.probablePrime(bits, random);		
			q = BigInteger.probablePrime(bits, random);
			n = p.multiply(q);
			System.out.println("bits= " + bits + " n= " + n);
			long start = System.nanoTime();
			Factors factors =  factorMethod.factor(n, sqrtMethod);
			long end = System.nanoTime();
			factors.getP();
			factors.getQ();
			if (p.compareTo(factors.getP()) == 0 ) {
				assertEquals(q, factors.getQ());
			} else {
				assertEquals(p, factors.getQ());
				assertEquals(q, factors.getP());
			}
			
			System.out.println("p= " + p + "\tq= " + q);
			System.out.println("Time: " + (end-start) +"\n\n");
			
			bits += 8;
		}
		  
	}
	
}
