package rsa.naive;

import java.math.BigInteger;

import rsa.api.PrimalityChecker;
import rsa.api.RandomNumberGenerator;
import rsa.api.RandomPrimeGenerator;

public final class NaiveRandomPrimeGenerator implements RandomPrimeGenerator {
	
	private final RandomNumberGenerator randomNumberGenerator;
	private final PrimalityChecker primalityChecker;
	
	public NaiveRandomPrimeGenerator(RandomNumberGenerator randomNumberGenerator, PrimalityChecker primalityChecker) {
		this.randomNumberGenerator = randomNumberGenerator;
		this.primalityChecker = primalityChecker;
	}

	@Override
	public BigInteger next() {
		BigInteger randomNumber;
		do {
			randomNumber = randomNumberGenerator.next();
		} while (!primalityChecker.test(randomNumber));
		return randomNumber;
	}

}
