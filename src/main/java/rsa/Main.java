package rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;

import rsa.api.BigIntegerSink;
import rsa.api.BigIntegerStream;
import rsa.api.BlockInputStream;
import rsa.api.BlockOutputStream;
import rsa.api.Key;
import rsa.api.RSACracker;
import rsa.api.RSAKey;
import rsa.api.RSAKeyGenerator;
import rsa.api.RSAKeyReader;
import rsa.api.RSAKeyWriter;
import rsa.api.RSAStrategy;
import rsa.api.StreamTransformer;
import rsa.naive.NaiveBigIntegerSinkToOutputStream;
import rsa.naive.NaiveBigIntegerStreamFromInputStream;
import rsa.naive.NaiveBlockInputStream;
import rsa.naive.NaiveBlockOutputStream;
import rsa.naive.NaiveRSAStrategy;

public class Main {

    private static final int KEY_BIT_LENGTH = 32;

    private static final RSAStrategy strategy = getRSAStrategy();

    private static RSAStrategy getRSAStrategy() {
        return new NaiveRSAStrategy(KEY_BIT_LENGTH);
    }

    public static void main(String[] args) throws Exception {
        String function = args[0];
        switch (function) {
            case "generate-key":
                generateKey(args);
                break;

            case "encode":
                encode(args);
                break;

            case "decode":
                decode(args);
                break;

            case "crack":
                crack(args);
                break;
        }
    }

    private static void generateKey(String[] args) throws Exception {
        String outputFileName = args[1];
        File outputFile = new File(outputFileName);

        RSAKeyGenerator rsaKeyGenerator = strategy.rsaKeyGenerator();
        RSAKeyWriter rsaKeyWriter = strategy.rsaKeyWriter();

        RSAKey key = rsaKeyGenerator.next();

        try (FileOutputStream out = new FileOutputStream(outputFile)) {
            rsaKeyWriter.write(key, out);
        }
    }

    private static void encode(String[] args) throws Exception {
        Function<Integer, Integer> calculateInputBitLength = a -> a - 1; // -1 to guarantee all blocks will be less than modulus
        Function<Integer, Integer> calculateOutputBitLength = Function.identity();
        Function<RSAKey, Key> getter = RSAKey::publicKey;

        // ---

        String rsaKeyFileName = args[1];
        String inputFileName = args[2];
        String outputFileName = args[3];

        RSAKeyReader rsaKeyReader = strategy.rsaKeyReader();
        StreamTransformer streamTransformer = strategy.streamTransformer();

        File rsaKeyFile = new File(rsaKeyFileName);
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        RSAKey rsaKey;
        try (FileInputStream rsaKeyInputStream = new FileInputStream(rsaKeyFile)) {
            rsaKey = rsaKeyReader.read(rsaKeyInputStream);
        }

        Key key = getter.apply(rsaKey);
        BigInteger exponent = key.exponent();
        BigInteger modulus = key.modulus();

        try (FileInputStream inputFileStream = new FileInputStream(inputFile);
                FileOutputStream outputFileStream = new FileOutputStream(outputFile)) {
            int bitLength = key.bitLength();
            int inputBitLength = calculateInputBitLength.apply(bitLength);
            int outputBitLength = calculateOutputBitLength.apply(bitLength);
            int inputBlockSizeInBytes = inputBitLength / 8;
            int outputBlockSizeInBytes = outputBitLength / 8;

            final BlockInputStream<IOException> blockInputStream = buildBlockInputStream(inputFileStream, inputBlockSizeInBytes);
            BlockOutputStream<IOException> blockOutputStream = buildBlockOutputStream(outputFileStream, outputBlockSizeInBytes);
            BigIntegerStream<IOException> in = buildBigIntegerStream(blockInputStream);
            BigIntegerSink<IOException> out = buildBigIntegerSink(blockOutputStream);

            streamTransformer.transform(in, out, inputNumber -> inputNumber.modPow(exponent, modulus));

            // an ending padding block is required
            if (blockInputStream.lastBlockSize() == inputBlockSizeInBytes) {
                byte[] paddingBlock = new byte[outputBlockSizeInBytes];
                Arrays.fill(paddingBlock, (byte) 0xFF);
                blockOutputStream.offer(paddingBlock);
            }
        }
    }

    private static void decode(String[] args) throws Exception {
        Function<Integer, Integer> calculateInputBitLength = Function.identity();
        Function<Integer, Integer> calculateOutputBitLength = a -> a - 1;
        Function<RSAKey, Key> getter = RSAKey::privateKey;

        // ---

        String rsaKeyFileName = args[1];
        String inputFileName = args[2];
        String outputFileName = args[3];

        RSAKeyReader rsaKeyReader = strategy.rsaKeyReader();
        StreamTransformer streamTransformer = strategy.streamTransformer();

        File rsaKeyFile = new File(rsaKeyFileName);
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        RSAKey rsaKey;
        try (FileInputStream rsaKeyInputStream = new FileInputStream(rsaKeyFile)) {
            rsaKey = rsaKeyReader.read(rsaKeyInputStream);
        }

        Key key = getter.apply(rsaKey);
        BigInteger exponent = key.exponent();
        BigInteger modulus = key.modulus();

        try (FileInputStream inputFileStream = new FileInputStream(inputFile);
                FileOutputStream outputFileStream = new FileOutputStream(outputFile)) {
            int bitLength = key.bitLength();
            int inputBitLength = calculateInputBitLength.apply(bitLength);
            int outputBitLength = calculateOutputBitLength.apply(bitLength);
            int inputBlockSizeInBytes = inputBitLength / 8;
            int outputBlockSizeInBytes = outputBitLength / 8;

            final BlockInputStream<IOException> blockInputStream = buildBlockInputStream(inputFileStream, inputBlockSizeInBytes);
            BlockOutputStream<IOException> blockOutputStream = buildBlockOutputStream(outputFileStream, outputBlockSizeInBytes);
            BlockOutputStream<IOException> wrappedBlockOutputStream = buffer -> {
                if (blockInputStream.hasNext()) {
                    blockOutputStream.offer(buffer);
                } else {
                    // handle padding information of last block
                    int i = buffer.length - 1;
                    for (; i >= 0 && buffer[i] == (byte) 0xFF; --i) {
                        ;
                    }
                    i--;
                    if (i > 0) {
                        return;
                    }
                    int qtyOfPaddedBytes = i;
                    int realBuferSize = buffer.length - qtyOfPaddedBytes;

                    byte[] bufferWithoutPaddingInformation = new byte[realBuferSize];
                    System.arraycopy(buffer, 0, bufferWithoutPaddingInformation, 0, realBuferSize);

                    int lastBlockSizeInBytes = outputBlockSizeInBytes - qtyOfPaddedBytes;
                    BlockOutputStream<IOException> lastBlockOutputStream = buildBlockOutputStream(outputFileStream, lastBlockSizeInBytes);
                    lastBlockOutputStream.offer(bufferWithoutPaddingInformation);
                }
            };
            BigIntegerStream<IOException> in = buildBigIntegerStream(blockInputStream);
            BigIntegerSink<IOException> out = buildBigIntegerSink(wrappedBlockOutputStream);

            streamTransformer.transform(in, out, inputNumber -> inputNumber.modPow(exponent, modulus));
        }
    }

    private static BlockOutputStream<IOException> buildBlockOutputStream(OutputStream outputStream, int blockSizeInBytes) {
        return new NaiveBlockOutputStream(outputStream, blockSizeInBytes);
    }

    private static BlockInputStream<IOException> buildBlockInputStream(InputStream inputStream, int blockSizeInBytes) {
        return new NaiveBlockInputStream(inputStream, blockSizeInBytes);
    }

    private static void crack(String[] args) throws Exception {
        String rsaKeyFileName = args[1];

        RSAKeyReader rsaKeyReader = strategy.rsaKeyReader();

        File rsaKeyFile = new File(rsaKeyFileName);

        RSAKey rsaKey;
        try (FileInputStream rsaKeyInputStream = new FileInputStream(rsaKeyFile)) {
            rsaKey = rsaKeyReader.read(rsaKeyInputStream);
        }

        Key publicKey = rsaKey.publicKey();

        RSACracker cracker = strategy.rsaCracker();
        Key privateKey = cracker.findPrivateKeyOf(publicKey);

        if (!privateKey.equals(rsaKey.privateKey())) {
            throw new RuntimeException();
        }
    }

    private static <E extends Throwable> BigIntegerSink<E> buildBigIntegerSink(BlockOutputStream<E> outputStream) {
        return new NaiveBigIntegerSinkToOutputStream<E>(outputStream);
    }

    private static <E extends Throwable> BigIntegerStream<E> buildBigIntegerStream(BlockInputStream<E> inputStream) {
        return new NaiveBigIntegerStreamFromInputStream<E>(inputStream);
    }

}
