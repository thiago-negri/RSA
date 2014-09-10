package rsa.api;

import java.math.BigInteger;

@FunctionalInterface
public interface BigIntegerSink {

	void offer(BigInteger number);
	
}
