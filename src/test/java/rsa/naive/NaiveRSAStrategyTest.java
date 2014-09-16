package rsa.naive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import rsa.api.BigIntegerSink;
import rsa.api.BigIntegerStream;
import rsa.api.BlockInputStream;
import rsa.api.BlockOutputStream;
import rsa.api.Key;
import rsa.api.RSAKey;
import rsa.api.RSAKeyGenerator;
import rsa.api.RSAStrategy;

public class NaiveRSAStrategyTest {

    @Test
    public void rsaKeyGeneration() {
        RSAStrategy strategy = new NaiveRSAStrategy(256);

        RSAKeyGenerator generator = strategy.rsaKeyGenerator();
        RSAKey key = generator.next();

        Key privateKey = key.privateKey();
        Key publicKey = key.publicKey();
        int bitLength = privateKey.bitLength();

        Random rnd = new Random();
        BigInteger in = new BigInteger(bitLength - 1, rnd);
        BigInteger encoded = in.modPow(publicKey.exponent(), publicKey.modulus());
        BigInteger decoded = encoded.modPow(privateKey.exponent(), privateKey.modulus());

        assertEquals(in, decoded);
    }

    @Test
    public void test() throws IOException {
        RSAStrategy strategy = new NaiveRSAStrategy(256);

        RSAKeyGenerator generator = strategy.rsaKeyGenerator();
        RSAKey key = generator.next();

        Key privateKey = key.privateKey();
        Key publicKey = key.publicKey();

        Random rnd = new Random();

        byte[] buf = new byte[(int) ((publicKey.bitLength() / 8.0d) * 1.35d)];
        rnd.nextBytes(buf);

        ByteArrayOutputStream encodedOutputStream = new ByteArrayOutputStream();
        { // encode
            int inputBlockSizeInBytes = (int) Math.ceil(publicKey.bitLength() / 8.0d) - 1;
            int outputBlockSizeInBytes = (int) Math.ceil(privateKey.bitLength() / 8.0d);
            InputStream inputStream = new ByteArrayInputStream(buf);
            BlockInputStream<IOException> blockInputStream = new NaiveBlockInputStream(inputStream, inputBlockSizeInBytes);
            BigIntegerStream<IOException> in = new NaiveBigIntegerStreamFromInputStream<IOException>(blockInputStream);
            BlockOutputStream<IOException> blockOutputStream = new NaiveBlockOutputStream(encodedOutputStream, outputBlockSizeInBytes);
            BigIntegerSink<IOException> out = new NaiveBigIntegerSinkToOutputStream<>(blockOutputStream);
            strategy.streamTransformer().transform(in, out, inNumber -> inNumber.modPow(publicKey.exponent(), publicKey.modulus()));
        }
        byte[] encodedBuf = encodedOutputStream.toByteArray();

        ByteArrayOutputStream decodedOutputStream = new ByteArrayOutputStream();
        { // decode
            int inputBlockSizeInBytes = (int) Math.ceil(privateKey.bitLength() / 8.0d);
            int outputBlockSizeInBytes = (int) Math.ceil(publicKey.bitLength() / 8.0d) - 1;
            InputStream inputStream = new ByteArrayInputStream(encodedBuf);
            BlockInputStream<IOException> blockInputStream = new NaiveBlockInputStream(inputStream, inputBlockSizeInBytes);
            BigIntegerStream<IOException> in = new NaiveBigIntegerStreamFromInputStream<IOException>(blockInputStream);
            BlockOutputStream<IOException> blockOutputStream = new NaiveBlockOutputStream(decodedOutputStream, outputBlockSizeInBytes);
            BigIntegerSink<IOException> out = new NaiveBigIntegerSinkToOutputStream<>(blockOutputStream);
            strategy.streamTransformer().transform(in, out, inNumber -> inNumber.modPow(privateKey.exponent(), privateKey.modulus()));
        }
        byte[] decodedBuf = decodedOutputStream.toByteArray();

        // remove padding
        int i = decodedBuf.length - 1;
        for (; i >= 0 && decodedBuf[i] == (byte) 0xFF; --i) {
            ;
        }
        decodedBuf = Arrays.copyOfRange(decodedBuf, 0, i);

        assertTrue(Arrays.equals(buf, decodedBuf));
    }
}
