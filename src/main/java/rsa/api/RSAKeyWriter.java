package rsa.api;

import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
public interface RSAKeyWriter {

	void write(RSAKey key, OutputStream out) throws IOException;
	
}
