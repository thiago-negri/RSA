package rsa.naive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

import rsa.api.BlockInputStream;

public final class NaiveBlockInputStream implements BlockInputStream<IOException> {

	private final InputStream inputStream;
	private final int blockSizeInBytes;
	private int lastBlockSize;

	public NaiveBlockInputStream(InputStream inputStream, int blockSizeInBytes) {
		this.inputStream = inputStream;
		this.blockSizeInBytes = blockSizeInBytes;
	}
	
	@Override
	public boolean hasNext() throws IOException {
		return inputStream.available() > 0;
	}

	@Override
	public Optional<byte[]> next() throws IOException {
		byte[] buffer = new byte[blockSizeInBytes];
		int readBytes = inputStream.read(buffer);
		if (readBytes < 0) {
			return Optional.empty();
		}
		if (readBytes < blockSizeInBytes) {
			// padding information in last block
			Arrays.fill(buffer, readBytes + 1, readBytes, (byte) 0xFF);
		}
		lastBlockSize = readBytes;
		return Optional.of(buffer);
	}

	@Override
	public int lastBlockSize() {
		return lastBlockSize;
	}

}
