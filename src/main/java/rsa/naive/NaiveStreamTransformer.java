package rsa.naive;

import java.math.BigInteger;
import java.util.Optional;

import rsa.api.BigIntegerSink;
import rsa.api.BigIntegerStream;
import rsa.api.BigIntegerTransformer;
import rsa.api.StreamTransformer;

public final class NaiveStreamTransformer implements StreamTransformer {

	@Override
	public <E extends Throwable> void transform(BigIntegerStream<E> in, BigIntegerSink<E> out, BigIntegerTransformer<E> transformer) throws E {
		Optional<BigInteger> optInputNumber = in.next();
		while (optInputNumber.isPresent()) {
			BigInteger inputNumber = optInputNumber.get();
			BigInteger outputNumber = transformer.transform(inputNumber);
			out.offer(outputNumber);
			optInputNumber = in.next();
		}
	}

}
