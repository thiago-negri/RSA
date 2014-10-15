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

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		//int repetition = 100;

		int bits = 16;
		System.out.println("CrackerEvaluation");
		PrintWriter writer = new PrintWriter("cracker-"+System.currentTimeMillis()+".csv", "UTF-8");

		writer.println("bits;nanoseconds");

	    final RSAStrategy strategy = new NaiveRSAStrategy();

		bits = 16;
		while (bits <= 64) {
			System.out.println("Evaluating  "+ bits + " key at " + new Date()); 
	        RSAKeyGenerator rsaKeyGenerator = strategy.rsaKeyGenerator(bits);
	        RSAKey rsaKey = rsaKeyGenerator.next();
	        Key publicKey = rsaKey.publicKey();

		        RSACracker cracker = strategy.rsaCracker(publicKey.bitLength());
		        long average = 0;
		        //for(int i =0; i < repetition; i++) {
		        	long start = System.nanoTime();
		        	Key privateKey = cracker.findPrivateKeyOf(publicKey);
		        	long end = System.nanoTime();
		        	average += (end - start);
		        	
		        //}
		        
		        //average = average/repetition;
				System.out.println("\tAvg Time: " + average + " ns");
				System.out.println("\tPrivate (" + privateKey.exponent() + ", " + privateKey.modulus()+ ")\n\tPublic (" + publicKey.exponent() + ", " + publicKey.modulus()+")\n\n");
				writer.println(bits+";"+average);
				bits += 1;
		}
		writer.close();

	}

}
