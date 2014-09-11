package rsa.naive;

import java.math.BigInteger;
import java.util.Optional;

import rsa.api.BigIntegerStream;
import rsa.api.BlockInputStream;

public final class NaiveBigIntegerStreamFromInputStream<E extends Throwable> implements BigIntegerStream<E> {

	private static final int SIGNUM_POSITIVE = 1;
	
	private final BlockInputStream<E> inputStream;

	public NaiveBigIntegerStreamFromInputStream(BlockInputStream<E> inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public Optional<BigInteger> next() throws E {
		return inputStream.next().map(buffer -> new BigInteger(SIGNUM_POSITIVE, buffer));
	}

	@Override
	public boolean hasNext() throws E {
		return inputStream.hasNext();
	}

}
