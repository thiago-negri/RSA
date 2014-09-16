package rsa.primality;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class MillerRabinTest {

    @Test
    public void falsePrimalityTest() {
        MillerRabin mr = new MillerRabin();
        assertFalse("is 221 prime?", mr.test(BigInteger.valueOf(221)));
        assertFalse("is 341 prime?", mr.test(BigInteger.valueOf(341)));
        assertFalse(mr.test(new BigInteger("1298074214633706835075030044377088")));

        // http://en.wikipedia.org/wiki/Catalan_pseudoprime
        assertFalse(mr.test(BigInteger.valueOf(5907)));
        assertFalse(mr.test(BigInteger.valueOf(1194649)));
        assertFalse(mr.test(BigInteger.valueOf(12327121)));
    }

    /**
     * 
     * http://www.bigprimes.net/archive/prime
     */
    @Test
    public void realPrimalityTest() {
        MillerRabin mr = new MillerRabin();
        assertTrue("is 2789 prime?", mr.test(BigInteger.valueOf(2789)));
        assertTrue("is 80737 prime?", mr.test(BigInteger.valueOf(80737)));
        assertTrue("is 1020379 prime?", mr.test(BigInteger.valueOf(1020379)));

        // Carol primes - http://en.wikipedia.org/wiki/List_of_prime_numbers
        assertTrue(mr.test(BigInteger.valueOf(7)));
        assertTrue(mr.test(BigInteger.valueOf(47)));
        assertTrue(mr.test(BigInteger.valueOf(223)));
        assertTrue(mr.test(BigInteger.valueOf(3967)));
        assertTrue(mr.test(BigInteger.valueOf(16127)));
        assertTrue(mr.test(BigInteger.valueOf(1046527)));
        assertTrue(mr.test(BigInteger.valueOf(16769023)));
        assertTrue(mr.test(BigInteger.valueOf(1073676287)));
        assertTrue(mr.test(new BigInteger("68718952447")));
        assertTrue(mr.test(new BigInteger("274876858367")));
        assertTrue(mr.test(new BigInteger("4398042316799")));
        assertTrue(mr.test(new BigInteger("1125899839733759")));
        assertTrue(mr.test(new BigInteger("18014398241046527")));
        assertTrue(mr.test(new BigInteger("1298074214633706835075030044377087")));

    }

}
