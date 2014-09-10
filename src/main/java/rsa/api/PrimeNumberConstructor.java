package rsa.api;

import java.math.BigInteger;
import java.util.Optional;

@FunctionalInterface
public interface PrimeNumberConstructor {

	Optional<PrimeNumber> build(BigInteger number);

}
