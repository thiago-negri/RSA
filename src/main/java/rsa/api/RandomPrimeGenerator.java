package rsa.api;

import java.math.BigInteger;

@FunctionalInterface
public interface RandomPrimeGenerator {

    BigInteger next();

}
