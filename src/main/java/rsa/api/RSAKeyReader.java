package rsa.api;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface RSAKeyReader {

	RSAKey read(InputStream in) throws IOException;
	
}
