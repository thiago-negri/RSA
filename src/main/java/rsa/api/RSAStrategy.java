package rsa.api;

public interface RSAStrategy {

	RSAKeyReader rsaKeyReader();

	RSAKeyWriter rsaKeyWriter();

	StreamTransformer streamTransformer();

	BigIntegerSinkConstructor bigIntegerSinkConstructor();

	BigIntegerStreamConstructor bigIntegerStreamConstructor();

	RSAKeyGenerator rsaKeyGenerator();

	RSACracker rsaCracker();

}
