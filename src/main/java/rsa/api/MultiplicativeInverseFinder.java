package rsa.api;

import java.math.BigInteger;
import java.util.Optional;

/** Extended Euclidian Algorithm */
public interface MultiplicativeInverseFinder {

    Optional<BigInteger> findMultiplicativeInverseOf(BigInteger number, BigInteger modulus);

}
