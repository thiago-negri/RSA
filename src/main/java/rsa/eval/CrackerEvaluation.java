package rsa.eval;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import rsa.api.Key;
import rsa.api.RSACracker;
import rsa.api.RSAKey;
import rsa.api.RSAKeyGenerator;
import rsa.api.RSAStrategy;
import rsa.naive.NaiveRSAStrategy;

public class CrackerEvaluation {

	private static final RSAStrategy strategy = new NaiveRSAStrategy();

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		int bits = 8;
		System.out.println("CrackerEvaluation");
		PrintWriter writer = new PrintWriter("cracker-"+System.currentTimeMillis()+".csv", "UTF-8");
		writer.println("bits;nanoseconds");

		System.out.println("=============== Warming up =============");
		warmup(32);

		System.out.println("=============== Measuring =============");
		while (bits <= 64) {
			RSAKeyGenerator rsaKeyGenerator = strategy.rsaKeyGenerator(bits);
			RSAKey rsaKey = rsaKeyGenerator.next();
			while (rsaKey.privateKey().modulus().bitLength() != bits) {
				System.out.println("Trying to get key with exactly" + bits + " bits");
				rsaKey = rsaKeyGenerator.next();
			}
			System.out.println("Evaluating  "+ rsaKey.privateKey().modulus().bitLength() + " key at " + new Date()); 
			Key publicKey = rsaKey.publicKey();

			RSACracker cracker = strategy.rsaCracker();
			long average = 0;
			long start = System.nanoTime();
			Key privateKey = cracker.findPrivateKeyOf(publicKey);
			long end = System.nanoTime();
			average += (end - start);
			
			System.out.println("\tAvg Time: " + average + " ns");
			System.out.println("\tPrivate (" + privateKey.exponent() + ", " + privateKey.modulus()+ ")\n\tPublic (" + publicKey.exponent() + ", " + publicKey.modulus()+")\n\n");

			if (!(privateKey.exponent().equals(rsaKey.privateKey().exponent()) || !(privateKey.modulus().equals(rsaKey.privateKey().modulus()))))
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Missed");

			writer.println(bits+";"+average);
			bits += 2;
		}
		writer.close();

	}

	private static void warmup(int warmupBits) {
		int bits = 8;
		while (bits <= warmupBits) {
			RSAKeyGenerator rsaKeyGenerator = strategy.rsaKeyGenerator(bits);
			RSAKey rsaKey = rsaKeyGenerator.next();
			System.out.println("Warming up  "+ rsaKey.privateKey().modulus().bitLength() + " key at " + new Date()); 
			Key publicKey = rsaKey.publicKey();

			RSACracker cracker = strategy.rsaCracker();
			long average = 0;
			long start = System.nanoTime();
			Key privateKey = cracker.findPrivateKeyOf(publicKey);
			long end = System.nanoTime();
			average += (end - start);

			System.out.println("\tAvg Time: " + average + " ns");
			System.out.println("\tPrivate (" + privateKey.exponent() + ", " + privateKey.modulus()+ ")\n\tPublic (" + publicKey.exponent() + ", " + publicKey.modulus()+")\n\n");

			bits += 2;
		}

	}

}
