package rsa.math.factorization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import rsa.math.sqrt.SqrtMethod;
import rsa.primality.EuclideanFinder;

//http://ijcsi.org/papers/IJCSI-8-6-1-242-247.pdf
public class AmbdekarFactorization implements FactorMethod {

	public Factors factor(BigInteger n, SqrtMethod sqrtMethod) {
		BigDecimal sqrt = sqrtMethod.sqrt(n);
		BigInteger ceil = sqrt.setScale(0, RoundingMode.CEILING).toBigInteger();   

		BigInteger square = null;
		while (true) {
			square = ceil.multiply(ceil).subtract(BigInteger.ONE);
			EuclideanFinder eculides = new EuclideanFinder();
			BigInteger gcd = eculides.greatestCommonDivisor(n, square);
			if (gcd.compareTo(BigInteger.ONE) == 1) {
				//gcd is a factor of n
				return new Factors(gcd, n.divide(gcd));
			}
			ceil = ceil.add(BigInteger.ONE);
		}
	}

}
