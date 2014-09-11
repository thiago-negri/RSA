package rsa.api;

import java.math.BigInteger;

public interface Key {

	BigInteger exponent();
	
	BigInteger modulus();

	int bitLength();
	
}
