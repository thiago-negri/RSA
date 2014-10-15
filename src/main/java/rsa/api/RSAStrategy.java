package rsa.api;

public interface RSAStrategy {

    RSAKeyReader rsaKeyReader();

    RSAKeyWriter rsaKeyWriter();

    StreamTransformer streamTransformer();

    RSAKeyGenerator rsaKeyGenerator(int keyBitLength);

    RSACracker rsaCracker(int keyBitLength);

}
