package rsa.naive;

import java.math.BigInteger;
import java.util.Optional;

import rsa.api.PrimeNumber;
import rsa.api.PrimeNumberConstructor;
import rsa.api.RandomNumberGenerator;
import rsa.api.RandomPrimeGenerator;

public final class NaiveRandomPrimeGenerator implements RandomPrimeGenerator {
	
	private final RandomNumberGenerator randomNumberGenerator;
	private final PrimeNumberConstructor primeNumberConstructor;
	
	public NaiveRandomPrimeGenerator(RandomNumberGenerator randomNumberGenerator, PrimeNumberConstructor primeNumberConstructor) {
		this.randomNumberGenerator = randomNumberGenerator;
		this.primeNumberConstructor = primeNumberConstructor;
	}

	@Override
	public PrimeNumber next() {
		Optional<PrimeNumber> optPrimeNumber;
		do {
			BigInteger randomNumber = randomNumberGenerator.next();
			optPrimeNumber = primeNumberConstructor.build(randomNumber);
		} while (!optPrimeNumber.isPresent());
		return optPrimeNumber.get();
	}

}
