package rsa.api;

@FunctionalInterface
public interface StreamTransformer {

	<E extends Throwable> void transform(Key key, BigIntegerStream<E> in, BigIntegerSink<E> out) throws E;

}
