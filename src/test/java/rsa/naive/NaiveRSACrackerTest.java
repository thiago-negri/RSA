package rsa.naive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import rsa.api.Key;
import rsa.api.RSAKey;
import rsa.api.RSAKeyGenerator;
import rsa.cracker.NaiveRSACracker;
import rsa.primality.EuclideanFinder;

public final class NaiveRSACrackerTest {

    @Test
    public void text() {
        RSAKeyGenerator keyGenerator = new NaiveRSAStrategy().rsaKeyGenerator(16);
        RSAKey next = keyGenerator.next();
        Key publicKey = next.publicKey();
        Key privateKey = next.privateKey();
        Key crackedPrivateKey = new NaiveRSACracker(new EuclideanFinder(), new EuclideanFinder()).findPrivateKeyOf(publicKey);
        assertEquals(privateKey.exponent(), crackedPrivateKey.exponent());
    }

}
