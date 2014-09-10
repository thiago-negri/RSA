package rsa.naive;

import java.math.BigInteger;

import rsa.api.Key;
import rsa.api.MultiplicativeInverseFinder;
import rsa.api.RSAKey;
import rsa.api.RSAKeyGenerator;
import rsa.api.RandomPrimeGenerator;
import rsa.api.RelativelyPrimeFinder;

public final class NaiveRSAKeyGenerator implements RSAKeyGenerator {

	private final RandomPrimeGenerator randomPrimeGenerator;
	private final RelativelyPrimeFinder relativelyPrimeFinder;
	private final MultiplicativeInverseFinder multiplicativeInverseFinder;

	public NaiveRSAKeyGenerator(RandomPrimeGenerator randomPrimeGenerator, RelativelyPrimeFinder relativelyPrimeFinder, MultiplicativeInverseFinder multiplicativeInverseFinder) {
		this.randomPrimeGenerator = randomPrimeGenerator;
		this.relativelyPrimeFinder = relativelyPrimeFinder;
		this.multiplicativeInverseFinder = multiplicativeInverseFinder;
	}
	
	@Override
	public RSAKey next() {
		RSAKey rsaKey = buildNewRSAKey();
		return rsaKey;
	}

	private RSAKey buildNewRSAKey() {
		BigInteger p = randomPrimeGenerator.next().rawValue();
		BigInteger q = randomPrimeGenerator.next().rawValue();
		BigInteger n = p.multiply(q); // this is the modulus
		BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // Euler's totient function
		BigInteger e = relativelyPrimeFinder.findRelativePrimeOf(phi_n); // `e` must be a coprime of `phi_n`
		BigInteger d = multiplicativeInverseFinder.findMultiplicativeInverseOf(e, phi_n); // `d` is the multiplicative inverse of `phi_n`, may be solved by extended Euclidian algorithm
		Key publicKey = new NaiveKey(e, n);
		Key privateKey = new NaiveKey(d, n);
		RSAKey rsaKey = new NaiveRSAKey(privateKey, publicKey);
		return rsaKey;
	}

}
