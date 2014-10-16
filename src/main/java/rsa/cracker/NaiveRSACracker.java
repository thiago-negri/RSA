package rsa.cracker;

import java.math.BigInteger;
import java.util.BitSet;

import rsa.api.Key;
import rsa.api.MultiplicativeInverseFinder;
import rsa.api.RSACracker;
import rsa.api.RelativelyPrimeFinder;
import rsa.naive.NaiveKey;

public final class NaiveRSACracker implements RSACracker {

    private final RelativelyPrimeFinder relativelyPrimeFinder;
    private final MultiplicativeInverseFinder multiplicativeInverseFinder;

    public NaiveRSACracker(RelativelyPrimeFinder relativelyPrimeFinder, MultiplicativeInverseFinder multiplicativeInverseFinder) {
        this.relativelyPrimeFinder = relativelyPrimeFinder;
        this.multiplicativeInverseFinder = multiplicativeInverseFinder;
    }

    @Override
    public Key findPrivateKeyOf(Key publicKey) {
        BigInteger n = publicKey.modulus();
        BigInteger top = sqrt(n).multiply(BigInteger.valueOf(2));
        BigInteger bottom = top.divide(BigInteger.valueOf(2));
        BigInteger trial = bottom;

        if (!trial.testBit(0)) trial = trial.add(BigInteger.ONE);

        BigInteger p = null;
        while (p == null && trial.compareTo(BigInteger.ZERO) > 0) {
            while (trial.compareTo(top) < 0 && !n.remainder(trial).equals(BigInteger.ZERO)) {
                trial = trial.nextProbablePrime();
                if (n.remainder(trial).equals(BigInteger.ZERO)) {
                    p = trial;
                    break;
                }
            }
            if (p == null) {
                top = bottom;
                bottom = top.divide(BigInteger.valueOf(2));
                trial = bottom;
            }
        }

        BigInteger q = n.divide(p);
        BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = relativelyPrimeFinder.findRelativePrimeOf(phi_n);
        BigInteger d = multiplicativeInverseFinder.findMultiplicativeInverseOf(e, phi_n).get();

        return new NaiveKey(d, n);
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
