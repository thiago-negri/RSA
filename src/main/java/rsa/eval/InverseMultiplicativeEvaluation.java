package rsa.eval;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

import rsa.naive.NaiveRandomNumberGenerator;
import rsa.primality.EuclideanFinder;

public class InverseMultiplicativeEvaluation {
	private static final EuclideanFinder euclideanFinder = new EuclideanFinder();



	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		int repetition = 100;

		int bits = 16;
		Random random = new Random();
		System.out.println("InverseMultiplicativeEvaluation");
		PrintWriter writer = new PrintWriter("inverser-mult"+System.currentTimeMillis()+".csv", "UTF-8");

		writer.println("bits;nanoseconds");
		while (bits <= 1024) {
			NaiveRandomNumberGenerator randomPrimeGenerator =  new NaiveRandomNumberGenerator(bits);
			BigInteger p = randomPrimeGenerator.next();
			BigInteger q = randomPrimeGenerator.next();
			//BigInteger n = p.multiply(q); // this is the modulus
			BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // Euler's totient function
			BigInteger e = euclideanFinder.findRelativePrimeOf(phi_n); // `e` must be a coprime of `phi_n`

			BigInteger number = BigInteger.probablePrime(bits, random);
			int average = 0;
			for(int i =0; i < repetition; i++) {
				average += eval(e, phi_n);
			}
			average = average/repetition;
			System.out.println("Bits: "+ number.bitLength() + "\tAvg Time: " + average + " ns");
			//writer.println(number.bitLength()+";"+average);
			bits += 16;
		}

		bits = 16;
		while (bits <= 1024) {
			NaiveRandomNumberGenerator randomPrimeGenerator =  new NaiveRandomNumberGenerator(bits);
			BigInteger p = randomPrimeGenerator.next();
			BigInteger q = randomPrimeGenerator.next();
			//BigInteger n = p.multiply(q); // this is the modulus
			BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // Euler's totient function
			BigInteger e = euclideanFinder.findRelativePrimeOf(phi_n); // `e` must be a coprime of `phi_n`

			BigInteger number = BigInteger.probablePrime(bits, random);
			int average = 0;
			for(int i =0; i < repetition; i++) {
				average += eval(e, phi_n);
			}
			average = average/repetition;
			System.out.println("Bits: "+ number.bitLength() + "\tAvg Time: " + average + " ns");
			writer.println(number.bitLength()+";"+average);
			bits += 16;
		}
		writer.close();

	}

	private static long eval(BigInteger a, BigInteger b) {
		long start = System.nanoTime();
		euclideanFinder.findMultiplicativeInverseOf(a, b);
		long end = System.nanoTime();
		return end - start;
	}

}
