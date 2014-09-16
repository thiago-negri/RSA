package rsa.naive;

import java.math.BigInteger;
import java.util.Arrays;

import rsa.api.BigIntegerSink;
import rsa.api.BlockOutputStream;

public final class NaiveBigIntegerSinkToOutputStream<E extends Throwable> implements BigIntegerSink<E> {

    private final BlockOutputStream<E> outputStream;

    public NaiveBigIntegerSinkToOutputStream(BlockOutputStream<E> outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void offer(BigInteger number) throws E {
        byte[] numberRepresentation = number.toByteArray();
        if (numberRepresentation[0] == 0) {
            numberRepresentation = Arrays.copyOfRange(numberRepresentation, 1, numberRepresentation.length);
        }
        outputStream.offer(numberRepresentation);
    }

}
