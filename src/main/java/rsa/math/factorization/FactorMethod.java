package rsa.math.factorization;

import java.math.BigInteger;

import rsa.math.sqrt.SqrtMethod;

public interface FactorMethod {
	
	public Factors factor(BigInteger n, SqrtMethod sqrtMethod);

}
