package rsa.naive;

import rsa.api.MultiplicativeInverseFinder;
import rsa.api.PrimalityChecker;
import rsa.api.RSACracker;
import rsa.api.RSAKeyGenerator;
import rsa.api.RSAKeyReader;
import rsa.api.RSAKeyWriter;
import rsa.api.RSAStrategy;
import rsa.api.RandomNumberGenerator;
import rsa.api.RandomPrimeGenerator;
import rsa.api.RelativelyPrimeFinder;
import rsa.api.StreamTransformer;
import rsa.primality.EuclideanFinder;
import rsa.primality.MillerRabin;

public final class NaiveRSAStrategy implements RSAStrategy {

    public NaiveRSAStrategy() {
    }

    @Override
    public RSAKeyReader rsaKeyReader() {
        return new NaiveRSAKeyReader();
    }

    @Override
    public RSAKeyWriter rsaKeyWriter() {
        return new NaiveRSAKeyWriter();
    }

    @Override
    public StreamTransformer streamTransformer() {
        return new NaiveStreamTransformer();
    }

    @Override
    public RSAKeyGenerator rsaKeyGenerator(int keyBitLength) {
        RandomPrimeGenerator randomPrimeGenerator = randomPrimeGenerator(keyBitLength);
        RelativelyPrimeFinder relativelyPrimeFinder = relativelyPrimeFinder();
        MultiplicativeInverseFinder multiplicativeInverseFinder = multiplicativeInverseFinder();
        return new NaiveRSAKeyGenerator(randomPrimeGenerator, relativelyPrimeFinder, multiplicativeInverseFinder);
    }

    @Override
    public RSACracker rsaCracker() {
        // TODO Auto-generated method stub
        return null;
    }

    private RandomPrimeGenerator randomPrimeGenerator(int keyBitLength) {
        RandomNumberGenerator randomNumberGenerator = randomNumberGenerator(keyBitLength);
        PrimalityChecker primalityChecker = primalityChecker();
        return new NaiveRandomPrimeGenerator(randomNumberGenerator, primalityChecker);
    }

    private RandomNumberGenerator randomNumberGenerator(int keyBitLength) {
        return new NaiveRandomNumberGenerator(keyBitLength);
    }

    private PrimalityChecker primalityChecker() {
        return new MillerRabin();
    }

    private RelativelyPrimeFinder relativelyPrimeFinder() {
        return new EuclideanFinder();
    }

    private MultiplicativeInverseFinder multiplicativeInverseFinder() {
        return new EuclideanFinder();
    }

}
