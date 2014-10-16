package rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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

    private static final RSAStrategy strategy = getRSAStrategy();

    private static RSAStrategy getRSAStrategy() {
        return new NaiveRSAStrategy();
    }

    public static void main(String[] args) throws Exception {
        String function = args[0];
        switch (function) {
            case "generate-key":
                generateKey(args);
                break;

            case "measure":
                measure(args);
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

            default:
                throw new RuntimeException(function);
        }
    }

    private static void measure(String[] args) throws Exception {
        String measureFileName = args[1];
        File measureFile = new File(measureFileName);
        int from = Integer.parseInt(args[2]);
        int to = Integer.parseInt(args[3]);
        int step = Integer.parseInt(args[4]);

        String[] args$ = new String[args.length - 5];
        System.arraycopy(args, 5, args$, 0, args$.length);

        int ix = -1;
        for (int i = 0; i < args$.length; ++i) {
            if (args$[i].equals("$")) {
                ix = i;
                break;
            }
        }

        if (ix < 0) {
            throw new RuntimeException();
        }

        try (PrintStream fos = new PrintStream(measureFile)) {
            fos.println("\"Command\",\"Ix\",\"Time (ns)\"");
            for (int i = from; i <= to; i += step) {
                args$[ix] = Integer.toString(i);

                StringBuilder cmdBuilder = new StringBuilder();
                cmdBuilder.append(args$[0]);
                for (int j = 1; j < args$.length; ++j) {
                    cmdBuilder.append(' ');
                    cmdBuilder.append(args$[j]);
                }
                String cmd = cmdBuilder.toString();

                System.out.println("> " + cmd);

                long a = System.nanoTime();
                main(args$);
                long b = System.nanoTime();
                fos.println("\"" + cmd + "\",\"" + i + "\",\"" + (b - a) + "\"");
            }
        }
    }

    private static void generateKey(String[] args) throws Exception {
        int keySizeInBits = Integer.parseInt(args[1]);

        RSAKeyGenerator rsaKeyGenerator = strategy.rsaKeyGenerator(keySizeInBits);
        RSAKeyWriter rsaKeyWriter = strategy.rsaKeyWriter();

        RSAKey key = rsaKeyGenerator.next();

        if (args.length > 2) {
            String outputFileName = args[2];
            if (!outputFileName.equals("stdout")) {
                File outputFile = new File(outputFileName);
                try (FileOutputStream out = new FileOutputStream(outputFile)) {
                    rsaKeyWriter.write(key, out);
                }
            } else {
                System.out.println("   public : " + key.publicKey().exponent());
                System.out.println("   private: " + key.privateKey().exponent());
                System.out.println("   modulus: " + key.privateKey().modulus());
            }
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
            int inputBlockSizeInBytes = (int) Math.ceil(inputBitLength / 8.0d) - 1;
            int outputBlockSizeInBytes = (int) Math.ceil(outputBitLength / 8.0d);

            System.out.println("Input block size: " + inputBitLength);
            System.out.println("Output block size: " + outputBitLength);
            
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
            int inputBlockSizeInBytes = (int) Math.ceil(inputBitLength / 8.0d);
            int outputBlockSizeInBytes = (int) Math.ceil(outputBitLength / 8.0d) - 1;

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
                    if (i <= 0) {
                        return;
                    }
                    int lastBlockSizeInBytes = i;
                    byte[] bufferWithoutPaddingInformation = Arrays.copyOfRange(buffer, 0, i);
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
