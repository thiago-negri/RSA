package rsa.api;

import java.math.BigInteger;

@FunctionalInterface
public interface BigIntegerSink<E extends Throwable> {

	void offer(BigInteger number) throws E;
	
}
