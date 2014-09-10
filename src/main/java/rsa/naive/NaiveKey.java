package rsa.naive;

import java.math.BigInteger;

import rsa.api.Key;

public final class NaiveKey implements Key {

	private final BigInteger exponent;
	private final BigInteger modulus;

	public NaiveKey(BigInteger exponent, BigInteger modulus) {
		this.exponent = exponent;
		this.modulus = modulus;
	}

	@Override
	public BigInteger exponent() {
		return exponent;
	}

	@Override
	public BigInteger modulus() {
		return modulus;
	}

}
