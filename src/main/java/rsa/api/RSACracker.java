package rsa.api;

@FunctionalInterface
public interface RSACracker {

    Key findPrivateKeyOf(Key publicKey);

}
