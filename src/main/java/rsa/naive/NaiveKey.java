package rsa.naive;

import java.math.BigInteger;

import rsa.api.Key;

public final class NaiveKey implements Key {

    private final BigInteger exponent;
    private final BigInteger modulus;
    private final int bitLength;

    public NaiveKey(BigInteger exponent, BigInteger modulus) {
        if (exponent.bitLength() > modulus.bitLength()) {
            throw new IllegalArgumentException();
        }
        this.exponent = exponent;
        this.modulus = modulus;
        bitLength = modulus.bitLength();
    }

    @Override
    public BigInteger exponent() {
        return exponent;
    }

    @Override
    public BigInteger modulus() {
        return modulus;
    }

    @Override
    public int bitLength() {
        return bitLength;
    }

}
