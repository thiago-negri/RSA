package rsa.eval;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

import rsa.primality.MillerRabin;

public class MillerRabinEvaluation {
	private static MillerRabin millerRabin = new MillerRabin();
	private static int accuracy = 50;
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		int repetition = 10;
		
		int bits = 16;
		Random random = new Random();
		
		PrintWriter writer = new PrintWriter("miller-rabin-"+System.currentTimeMillis()+".csv", "UTF-8");
		
		writer.println("bits;nanoseconds");
		while (bits <= 1024) {
			BigInteger number = BigInteger.probablePrime(bits, random);
			long average = 0;
			for(int i =0; i < repetition; i++) {
				average += eval(number);
			}
			average = average/repetition;
			System.out.println("Bits: "+ number.bitLength() + "\tAvg Time: " + average + " ns");
			writer.println(number.bitLength()+";"+average);
			bits += 16;
		}
		writer.close();
		
		Double accuracy = new Double(3.0/4.0);
		for(int i=0; i < MillerRabin.ACCURACY; i++) {
			accuracy *= 3.0/4.0;
		}
		System.out.println("Probability: " + accuracy);
		
	}
	
	private static long eval(BigInteger n) {
		long start = System.nanoTime();
		boolean isPrime = millerRabin.test(n, accuracy);
		long end = System.nanoTime();
		return isPrime ? end - start : -1;
	}

}
