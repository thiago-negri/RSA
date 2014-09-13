package rsa.primality;

import java.math.BigInteger;

import rsa.api.RelativelyPrimeFinder;

public class Euclid implements RelativelyPrimeFinder {

	@Override
	public BigInteger findRelativePrimeOf(BigInteger number) {
		//http://stackoverflow.com/questions/19328748/calculating-a-relative-prime-of-a-number-in-java
		//http://pt.wikipedia.org/wiki/N%C3%BAmeros_primos_entre_si
		//n-1 and n+2 will be a relative prime.
		//TODO: This seems to be to easy to be an RSA implementation and as I could see we need a odd relative prime.
		//Still implemented method are giving very low odd numbers...
		
		return findRelativeOddPrimeOf(number);
	}

	
	public BigInteger findRelativeOddPrimeOf(BigInteger number) {
		BigInteger oddRelativePrime = BigInteger.valueOf(3);
		BigInteger gcd = greatestCommonDivisor(number, oddRelativePrime);
		BigInteger two = BigInteger.valueOf(2);
		while(! gcd.equals(BigInteger.ONE)) {
			oddRelativePrime = oddRelativePrime.add(two);
			gcd = greatestCommonDivisor(number, oddRelativePrime);
		}
		return oddRelativePrime;
	}
	
	
	/**
	 * The GCD of two positive integers is the largest integer that divides both of them without leaving a remainder .
	 * This method apllies the Euclidean division in order to obtain the GCD since it more efficent:
	 * "Euclidean division reduces all the steps between two exchanges into a single step, which is thus more efficient"
	 * http://en.wikipedia.org/wiki/Euclidean_algorithm
	 * http://en.wikipedia.org/wiki/Euclidean_division
	 * 
	 * @param n1
	 * @param n2
	 * @return
	 */
	public BigInteger greatestCommonDivisor(final BigInteger n1, final BigInteger n2) {
		BigInteger tmp = BigInteger.ZERO;
		BigInteger a = n1;
		BigInteger b = n2; 
		//Repeat until we hit an integer division
		while (!b.equals(BigInteger.ZERO)) {
			tmp = b;
			b = a.mod(b); //Calculates the latest remainder (k-1)
			a = tmp; //Set the next remainder k 
		}
		return a;
	}
	
}
