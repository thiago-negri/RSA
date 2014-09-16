package rsa.primality;

import java.math.BigInteger;
import java.util.Optional;

import rsa.api.MultiplicativeInverseFinder;
import rsa.api.RelativelyPrimeFinder;

public class EuclideanFinder implements RelativelyPrimeFinder, MultiplicativeInverseFinder {

    /**
     * The inverse multiple method is based on Euclidean Extended Algorithm: ax + by = mdc(a, b)
     * Considering a GCD(a,b) = 1 (relative primes) and x, y integers where ax + by = 1:
     * ax is congruent to 1 (mod n)
     * x is the multiplicative inverse of a (mod n)
     * https://www.youtube.com/watch?v=oRwuQrm3gqE
     */
    @Override
    public Optional<BigInteger> findMultiplicativeInverseOf(BigInteger a, BigInteger b) {
        BigInteger inverse = BigInteger.ZERO;
        BigInteger newInverse = BigInteger.ONE;

        BigInteger remainder = b;
        BigInteger newRemainder = a;

        BigInteger quotient = null;
        BigInteger aux1;

        // Repeat the process while we did not find the GCD
        while (!newRemainder.equals(BigInteger.ZERO)) {
            quotient = remainder.divide(newRemainder);

            // Performs the calculation of the inverse candidate (x) based on Euclidean Extended Algorithm  
            aux1 = newInverse;
            newInverse = inverse.subtract(quotient.multiply(newInverse));
            inverse = aux1;

            // Update remainder in order to get the new quotient
            aux1 = newRemainder;
            newRemainder = remainder.subtract(quotient.multiply(newRemainder));
            remainder = aux1;
        }
        if (remainder.compareTo(BigInteger.ONE) > 0) {
            // We don't have a GDC = 1
            return Optional.empty();
        }

        if (inverse.compareTo(BigInteger.ZERO) < 0) {
            // We found the inverse but we dont want a negative number
            // This operation is done due congruent characteristics where every integer t+b will fit as a inverse of a mod b   
            inverse = inverse.add(b);
        }
        return Optional.of(inverse);
    }

    @Override
    public BigInteger findRelativePrimeOf(BigInteger number) {
        // http://stackoverflow.com/questions/19328748/calculating-a-relative-prime-of-a-number-in-java
        // http://pt.wikipedia.org/wiki/N%C3%BAmeros_primos_entre_si
        // n-1 and n+2 will be a relative prime.
        // TODO: This seems to be to easy to be an RSA implementation and as I could see we need a odd relative prime.
        // Current implementation gives very low odd numbers...
        return findRelativeOddPrimeOf(number);
    }

    public BigInteger findRelativeOddPrimeOf(BigInteger number) {
        BigInteger oddRelativePrime = BigInteger.valueOf(3);
        BigInteger gcd = greatestCommonDivisor(number, oddRelativePrime);
        BigInteger two = BigInteger.valueOf(2);
        while (!gcd.equals(BigInteger.ONE)) {
            oddRelativePrime = oddRelativePrime.add(two);
            gcd = greatestCommonDivisor(number, oddRelativePrime);
        }
        return oddRelativePrime;
    }

    /**
     * The GCD of two positive integers is the largest integer that divides both of them without leaving a remainder .
     * This method apllies the Euclidean division in order to obtain the GCD since it more efficent:
     * "Euclidean division reduces all the steps between two exchanges into a single step, which is thus more efficient"
     * http://en.wikipedia.org/wiki/Euclidean_algorithm
     * http://en.wikipedia.org/wiki/Euclidean_division
     */
    public BigInteger greatestCommonDivisor(final BigInteger n1, final BigInteger n2) {
        BigInteger tmp = BigInteger.ZERO;
        BigInteger a = n1;
        BigInteger b = n2;
        // Repeat until we hit an integer division
        while (!b.equals(BigInteger.ZERO)) {
            tmp = b;
            b = a.mod(b); // Calculates the latest remainder (k-1)
            a = tmp; // Set the next remainder k 
        }
        return a;
    }

}
