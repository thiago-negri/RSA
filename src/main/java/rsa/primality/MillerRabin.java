package rsa.primality;

import java.math.BigInteger;
import java.util.Random;

import rsa.api.PrimalityChecker;

/**
 * The Miller–Rabin primality test is a primality test: an algorithm which determines whether a given number is prime.
 * Current implementation is based on following resources:
 * - http://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test
 * - http://www.ime.usp.br/~cris/aulas/06_2_5776/notas-de-aula/primos.pdf
 * 
 * @author marciogj
 */
public class MillerRabin implements PrimalityChecker {

    private static final int ACCURACY = 50;
    private final Random random;

    public MillerRabin() {
        random = new Random();
    }

    @Override
    public boolean test(BigInteger t) {
        return test(t, ACCURACY);
    }

    /**
     * Evaluates if the input is a probable prime number. Although a true output does not guarantee that input is prime,
     * the accuracy
     * parameter might give you some certainty.
     * 
     * @param n A number N which will be tested as a probable prime
     * @param accuracy Number of times that Miller Rabin evaluation will be performed to increase the accuracy of the
     *            primality
     * @return false if input number is definitely a composite (not prime); true if it is a probable prime (according to
     *         accuracy evaluation)
     */
    public boolean test(BigInteger n, final int accuracy) {
        boolean isProbablePrime = true;
        int loopAccuracy = 0;
        while (isProbablePrime && loopAccuracy <= accuracy) {
            isProbablePrime &= testMillerRabin(n);
            if (!isProbablePrime) {
                return false;
            }
            loopAccuracy++;
        }
        return isProbablePrime;
    }

    /**
     * Perform a single evaluation of N which increase around 1/4 of primality.
     * 
     * @param A number N which will be tested as a probable prime
     * @return false if input number is definitely a composite (not prime); true if it is a probable prime (according to
     *         accuracy evaluation)
     */
    private boolean testMillerRabin(final BigInteger n) {
        BigInteger nMinusOne = n.subtract(BigInteger.ONE);
        BigInteger a = BigInteger.ZERO;

        // Step 1: Create a random number between 1 and n-1. Following property must be ensured:  1 < a < n-1
        while (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(nMinusOne) >= 0) {
            a = new BigInteger(n.bitLength(), random);
        }

        // Step 2: Obtain 2 numbers according to the formula: n-1 = (2^s)*d, where:
        // s - is a integer obtained from n-1 factoring: (n-1)/2/2/2... - we stop when division gives a float.    
        // d - is a odd integer obtained to compete to equation d = (n-1) / 2^s - since n and s are already known
        int s = factorOfTwo(nMinusOne);
        BigInteger d = nMinusOne.divide(BigInteger.valueOf(2).pow(s));

        // Step 3: This step root is based on Euclids lemma and Fermat theorem that result in
        // If a^d is not congruent to 1 (mod n) - then n is a composite
        // If a^(2*2*...*2*d) is not congruent to -1 (mod n) - then n is a composite as well
        // Otherwise the number seems to be a prime unlesse we have a liar number
        BigInteger x = a.modPow(d, n); //x = a^d mod n

        // Checks whether a^d is congruent to 1 (mod n) or x = n-1 which is congurent to -1 (mod n)
        // If one of above rules matches, it´s a pseudo prime. New verification might be performed with another a
        if (x.equals(BigInteger.ONE) || x.equals(nMinusOne)) {
            return true;
        }

        // Performs a verification to find a witness of n composition unless we reach (n-1) which indicates a prime probability
        // This evaluation also uses the definition that a^(2*2*...*2 * d) must be congruent to -1 (mod n)   
        for (int i = 0; i < s - 1; i++) {
            x = x.modPow(BigInteger.valueOf(2), n); // x = x^(2*d) mod n
            if (x.equals(BigInteger.ONE)) {
                return false; // 1 is NOT congruent to -1 (mod n) - This is a composite number
            }
            if (x.equals(nMinusOne)) {
                return true; // (n-1) is congruent to -1 (mod n) - We have a prime chance
            }
        }
        return false; // it's a composite since n-1 was not reached.
    }

    /**
     * Factor a number (using divisor number 2) while the division result is zero.
     * 
     * @param m The number to be factored
     * @return The factor of 2 of input number.
     */
    private int factorOfTwo(final BigInteger m) {
        BigInteger two = BigInteger.valueOf(2);
        BigInteger factored = m;
        int factor = 0;
        while (factored.mod(two).equals(BigInteger.ZERO)) {
            factored = factored.divide(two);
            factor++;
        }
        return factor;
    }

}
