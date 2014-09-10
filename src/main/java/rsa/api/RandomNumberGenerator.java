package rsa.api;

import java.math.BigInteger;

@FunctionalInterface
public interface RandomNumberGenerator {

	BigInteger next();
	
}
