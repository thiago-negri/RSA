package rsa.primality;

import java.math.BigInteger;
import java.util.Random;

import rsa.api.PrimalityChecker;

/**
 * The Miller–Rabin primality test or Rabin–Miller primality test is a primality test: an algorithm which determines whether a given number is prime.
 * 
 * Details: http://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test
 * @author marciogj
 *
 */
public class MillerRabin implements PrimalityChecker {
	private static final int ACCURACY = 50; 
	private final Random random;

	public MillerRabin() {
		random = new Random();
	}

	@Override
	public boolean test(BigInteger t) {
		return test(t, ACCURACY);
	}

	public boolean test(BigInteger n, final int accuracy) {
		System.out.println("n = " + n);
		boolean isProbablePrime = true;
		int loopAccuracy = 0;
		while (isProbablePrime && loopAccuracy <= accuracy) {
			System.out.println("accuracy " + loopAccuracy + "/" + accuracy);
			isProbablePrime &= testMillerRabin(n);
			if (!isProbablePrime) return false;
			loopAccuracy++;
		}

		return isProbablePrime;
	}

	/**
	 * Avalia se o numero N é possivelmente primo ou composto (não primo). 
	 * @param n
	 * @return
	 */
	private boolean testMillerRabin(final BigInteger n) {
		BigInteger nMinusOne = n.subtract(BigInteger.ONE);
		BigInteger a = BigInteger.ZERO;
		//Obter um numero aleatório entre 1 e n-1. Ou seja:  1 < a < n-1
		while (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(nMinusOne) >= 0) { 
			a = new BigInteger(n.bitLength(), random);
			System.out.println("a = " + a);
		}

		// n-1 = (2^s)*d
		//s - é um inteiro qualquer 
		//d - é um inteiro impar por definição (a parte par será de s)
		// d = (n-1) / 2^s
		int s = factorOfTwo(nMinusOne);
		BigInteger d = nMinusOne.divide(BigInteger.valueOf(2).pow(s));
		System.out.println(nMinusOne + "  = 2^" + s + " * " + d);
		 
		BigInteger x = a.modPow(d, n); //x = a^d mod n
		System.out.println("x = " + x);
		//2^0
		if (x.equals(BigInteger.ONE) || x.equals(nMinusOne)) return true; // deve gerar um novo a

		for (int i=0; i < s-1; i++) {
			x = x.modPow(BigInteger.valueOf(2), n); //x^2 mod n
			System.out.println("x = " + x);
			if (x.equals(BigInteger.ONE)) return false; //é um numero composto
			if (x.equals(nMinusOne)) return true; // deve gerar um novo a
		}

		return false; //é um numero composto
	}

	private int factorOfTwo(final BigInteger m) {
		BigInteger two = BigInteger.valueOf(2);
		BigInteger factored = m;
		int factor = 0;
		while (factored.mod(two).equals(BigInteger.ZERO)) {
			factored = factored.divide(two);
			factor++;
		}
		return factor;
	}
	
	
}
