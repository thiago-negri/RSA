package rsa.api;

import java.io.OutputStream;

@FunctionalInterface
public interface RSAKeyWriter {

	void write(RSAKey key, OutputStream out);
	
}
