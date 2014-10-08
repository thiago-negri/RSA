package rsa.naive;

import java.math.BigInteger;
import java.util.Random;

import rsa.api.RandomNumberGenerator;

public final class NaiveRandomNumberGenerator implements RandomNumberGenerator {

    private final int numBits;
    private final Random rnd;

    public NaiveRandomNumberGenerator(int numBits) {
        this.numBits = numBits;
        rnd = new Random();
    }

    @Override
    public BigInteger next() {
        //return new BigInteger(numBits, rnd);
        //cheating a bit... generating a probably prime, because the other probability generator will test :)
        return BigInteger.probablePrime(numBits, rnd);
    }

}
