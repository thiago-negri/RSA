package rsa.api;

@FunctionalInterface
public interface StreamTransformer {

	void transform(Key key, BigIntegerStream in, BigIntegerSink out);

}
