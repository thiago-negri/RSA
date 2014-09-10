package rsa.api;

import java.math.BigInteger;

/** Extended Euclidian Algorithm */
public interface MultiplicativeInverseFinder {

	BigInteger findMultiplicativeInverseOf(BigInteger number, BigInteger modulus);

}
