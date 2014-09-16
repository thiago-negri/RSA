package rsa.cracker;

import java.math.BigInteger;
import java.util.BitSet;

import rsa.api.Key;
import rsa.api.RSACracker;
import rsa.naive.NaiveKey;

public final class NaiveRSACracker implements RSACracker {

    // WORK IN PROGRESS

    public static void main(String[] args) {
        Key publicKey = new NaiveKey(new BigInteger("3"), new BigInteger("637642431782093011"));
        new NaiveRSACracker().findPrivateKeyOf(publicKey);
    }

    @Override
    public Key findPrivateKeyOf(Key publicKey) {
        BigInteger n = publicKey.modulus();
        BigInteger sqrt = sqrt(n);
        System.out.println("Sqrt: " + sqrt);
        BigInteger trial = sqrt.add(BigInteger.valueOf(4));
        BigInteger p;
        if (trial.remainder(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) /* is pair */{
            trial = trial.add(BigInteger.ONE);
        }
        p = trial;
        while (!n.remainder(trial).equals(BigInteger.ZERO)) {
            trial = trial.subtract(BigInteger.valueOf(2));
            p = trial;
        }
        BigInteger q = n.divide(p);

        System.out.println("p: " + p);
        System.out.println("q: " + q);
        return null;
    }

    private BigInteger sqrt(BigInteger num) {
        // DON'T EVEN ASK OR TRY TO UNDERSTAND
        // WIKIPEDIA FOR THE RESCUE
        // - https://en.wikipedia.org/wiki/Methods_of_computing_square_roots

        BigInteger res = BigInteger.ZERO;

        int i = 2;

        BitSet bitSet = new BitSet();
        bitSet.set(i);

        // "bit" starts at the highest power of four <= the argument.
        BigInteger bit = new BigInteger(bitSet.toByteArray());
        BigInteger lastBit = bit;
        while (bit.compareTo(num) < 0) {
            lastBit = bit;
            bit = bit.multiply(BigInteger.valueOf(4L));
        }

        bit = lastBit;

        while (!bit.equals(BigInteger.ZERO)) {
            System.out.println("In loop at " + bit);
            BigInteger resPlusBit = res.add(bit);
            if (num.compareTo(resPlusBit) >= 0) {
                num = num.subtract(resPlusBit);
                res = (res.divide(BigInteger.valueOf(2))).add(bit);
            } else {
                res = res.divide(BigInteger.valueOf(2));
            }
            bit = bit.divide(BigInteger.valueOf(4));
        }
        return res;
    }

}
