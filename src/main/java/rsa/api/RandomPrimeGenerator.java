package rsa.api;

@FunctionalInterface
public interface RandomPrimeGenerator {

	PrimeNumber next();
	
}
