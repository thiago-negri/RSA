package rsa.api;

@FunctionalInterface
public interface StreamTransformer {

    <E extends Throwable> void transform(BigIntegerStream<E> in, BigIntegerSink<E> out, BigIntegerTransformer<E> transformer) throws E;

}
