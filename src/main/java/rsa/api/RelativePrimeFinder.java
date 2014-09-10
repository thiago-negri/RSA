package rsa.api;

import java.math.BigInteger;

@FunctionalInterface
public interface RelativePrimeFinder {

	BigInteger findRelativePrimeOf(BigInteger number);
	
}
