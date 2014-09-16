package rsa.naive;

import java.io.IOException;
import java.io.OutputStream;

import rsa.api.BlockOutputStream;

public final class NaiveBlockOutputStream implements BlockOutputStream<IOException> {

    private final OutputStream outputStream;
    private final int blockSizeInBytes;

    public NaiveBlockOutputStream(OutputStream outputStream, int blockSizeInBytes) {
        this.outputStream = outputStream;
        this.blockSizeInBytes = blockSizeInBytes;
    }

    @Override
    public void offer(byte[] buffer) throws IOException {
        if (buffer.length > blockSizeInBytes) {
            throw new IllegalArgumentException();
        }
        byte[] bufferToWrite;
        if (buffer.length < blockSizeInBytes) {
            int padding = blockSizeInBytes - buffer.length;
            bufferToWrite = new byte[blockSizeInBytes];
            System.arraycopy(buffer, 0, bufferToWrite, padding, buffer.length);
        } else {
            bufferToWrite = buffer;
        }
        outputStream.write(bufferToWrite);
    }

}
