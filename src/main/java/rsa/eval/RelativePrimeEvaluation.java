package rsa.eval;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

import rsa.primality.EuclideanFinder;

public class RelativePrimeEvaluation {
	private static EuclideanFinder euclideanFinder = new EuclideanFinder();
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
		int repetition = 100;
		
		int bits = 16;
		Random random = new Random();
		System.out.println("RelativePrimeEvaluation");
		PrintWriter writer = new PrintWriter("relative-prime-"+System.currentTimeMillis()+".csv", "UTF-8");
		
		writer.println("bits;nanoseconds");
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		//TODO: Verificar se há outra forma de medir sem ruidos.
		//Percebi que os numeros iniciais tinham tempo alto e o valor parecia estabilizar no fim da execução.
		//Com a execução feita 2x (a sem escrever em arquivo), a medição ficou estabilizada e distribuida o que me parece mais correto (os ruidos podem ser da iniclização da JVM ou cache).
		while (bits <= 1024) {
			BigInteger number = BigInteger.probablePrime(bits, random);
			long average = 0;
			for(int i =0; i < repetition; i++) {
				average += eval(number);
			}
			average = average/repetition;
			System.out.println("Bits: "+ number.bitLength() + "\tAvg Time: " + average + " ns");
			//writer.println(number.bitLength()+";"+average);
			bits += 16;
		}
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		
		bits = 16;
		while (bits <= 1024) {
			BigInteger number = BigInteger.probablePrime(bits, random);
			int average = 0;
			for(int i =0; i < repetition; i++) {
				average += eval(number);
			}
			average = average/repetition;
			System.out.println("Bits: "+ number.bitLength() + "\tAvg Time: " + average + " ns");
			writer.println(number.bitLength()+";"+average);
			bits += 16;
		}
		writer.close();
		
	}
	
	private static long eval(BigInteger n) {
		long start = System.nanoTime();
		euclideanFinder.findRelativeOddPrimeOf(n);
		long end = System.nanoTime();
		return end - start;
	}

}
