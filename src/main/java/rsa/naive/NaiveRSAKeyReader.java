package rsa.naive;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;

import rsa.api.RSAKey;
import rsa.api.RSAKeyReader;

public final class NaiveRSAKeyReader implements RSAKeyReader {

	@Override
	public RSAKey read(InputStream in) throws IOException {
		Properties properties = new Properties();
		properties.load(in);
		String rawPrivateExponent = properties.getProperty("privateExponent");
		String rawPublicExponent = properties.getProperty("publicExponent");
		String rawModulus = properties.getProperty("modulus");
		BigInteger privateExponent = new BigInteger(rawPrivateExponent);
		BigInteger publicExponent = new BigInteger(rawPublicExponent);
		BigInteger modulus = new BigInteger(rawModulus);
		RSAKey rsaKey = new NaiveRSAKey(publicExponent, privateExponent, modulus);
		return rsaKey;
	}

}
