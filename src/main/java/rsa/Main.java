package rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

import rsa.api.BigIntegerSink;
import rsa.api.BigIntegerStream;
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

public class Main {

	private static final RSAStrategy strategy = getRSAStrategy();
	private static RSAStrategy getRSAStrategy() {
		return null;
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
		transform(args, RSAKey::publicKey);
	}
	
	private static void decode(String[] args) throws Exception {
		transform(args, RSAKey::privateKey);
	}
	
	public static void transform(String[] args, Function<RSAKey, Key> getter) throws Exception {
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
		
		try (FileInputStream inputFileStream = new FileInputStream(inputFile);
			 FileOutputStream outputFileStream = new FileOutputStream(outputFile)
			) {
			Key key = getter.apply(rsaKey);
			
			// -1 to guarantee that the block will never have a number bigger than the modulus
			int bitLength = key.bitLength() - 1;
			
			BigIntegerStream<IOException> in = buildBigIntegerStream(inputFileStream, bitLength);
			BigIntegerSink<IOException> out = buildBigIntegerSink(outputFileStream);
			
			streamTransformer.transform(key, in, out);
		}
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

	private static BigIntegerSink<IOException> buildBigIntegerSink(OutputStream outputStream) {
		return new NaiveBigIntegerSinkToOutputStream(outputStream);
	}

	private static BigIntegerStream<IOException> buildBigIntegerStream(InputStream inputStream, int bitLength) {
		return new NaiveBigIntegerStreamFromInputStream(inputStream, bitLength);
	}
	
}
