package rsa.math.factorization;

import java.math.BigInteger;

import rsa.math.sqrt.SqrtMethod;

public class NaiveFactorization implements FactorMethod {

	
	public Factors factor(BigInteger n, SqrtMethod sqrtMethod) {
		BigInteger sqrt = sqrtMethod.sqrt(n).toBigInteger();
        BigInteger trial = sqrt.add(BigInteger.valueOf(4));
        BigInteger p;
        if (trial.remainder(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) /* is pair */{
            trial = trial.add(BigInteger.ONE);
        }
        p = trial;
        while (n.compareTo(BigInteger.ZERO) > 0 && !n.remainder(trial).equals(BigInteger.ZERO)) {
            trial = trial.subtract(BigInteger.valueOf(2));
            p = trial;
        }
        return new Factors(p, n.divide(p));
	}
	
}
