package rsa.api;

@FunctionalInterface
public interface RSAKeyGenerator {

    RSAKey next();

}
