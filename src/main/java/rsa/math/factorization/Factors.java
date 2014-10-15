package rsa.math.factorization;

import java.math.BigInteger;

public class Factors {
	BigInteger p;
	BigInteger q;
	
	
	public Factors(BigInteger p, BigInteger q) {
		super();
		this.p = p;
		this.q = q;
	}
	
	public BigInteger getP() {
		return p;
	}
	public BigInteger getQ() {
		return q;
	}
	
	

}
