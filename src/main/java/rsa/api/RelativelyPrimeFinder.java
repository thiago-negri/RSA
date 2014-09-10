package rsa.api;

import java.math.BigInteger;

@FunctionalInterface
public interface RelativelyPrimeFinder {

	BigInteger findRelativePrimeOf(BigInteger number);
	
}
