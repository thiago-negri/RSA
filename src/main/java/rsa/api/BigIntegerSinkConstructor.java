package rsa.api;

import java.io.OutputStream;

@FunctionalInterface
public interface BigIntegerSinkConstructor {

	BigIntegerSink build(OutputStream out);
	
}
