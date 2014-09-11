package rsa.api;

import java.math.BigInteger;
import java.util.Optional;

@FunctionalInterface
public interface BigIntegerStream<E extends Throwable> {

	Optional<BigInteger> next() throws E;
	
}
