package rsa.naive;

import java.math.BigInteger;

import rsa.api.Key;
import rsa.api.RSAKey;

public final class NaiveRSAKey implements RSAKey {

	private final BigInteger publicExponent;
	private final BigInteger privateExponent;
	private final BigInteger modulus;

	public NaiveRSAKey(BigInteger publicExponent, BigInteger privateExponent, BigInteger modulus) {
		this.publicExponent = publicExponent;
		this.privateExponent = privateExponent;
		this.modulus = modulus;
	}

	@Override
	public Key privateKey() {
		return new NaiveKey(privateExponent, modulus);
	}

	@Override
	public Key publicKey() {
		return new NaiveKey(publicExponent, modulus);
	}

}
