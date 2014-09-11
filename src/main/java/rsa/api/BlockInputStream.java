package rsa.api;

import java.util.Optional;

public interface BlockInputStream<E extends Throwable> {

	boolean hasNext() throws E;

	Optional<byte[]> next() throws E;

	int lastBlockSize();
	
}
