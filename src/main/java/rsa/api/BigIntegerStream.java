package rsa.api;

import java.math.BigInteger;
import java.util.Optional;

@FunctionalInterface
public interface BigIntegerStream {

	Optional<BigInteger> next();
	
}
