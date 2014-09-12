package rsa.naive;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

import rsa.api.Key;
import rsa.api.RSAKey;
import rsa.api.RSAKeyWriter;

public final class NaiveRSAKeyWriter implements RSAKeyWriter {

	@Override
	public void write(RSAKey key, OutputStream out) throws IOException {
		Key privateKey = key.privateKey();
		Key publicKey = key.publicKey();
		BigInteger privateExponent = privateKey.exponent();
		BigInteger publicExponent = publicKey.exponent();
		BigInteger modulus = privateKey.modulus();
		StringBuilder sb = new StringBuilder();
		sb.append("privateExponent=").append(privateExponent).append("\r\n");
		sb.append("publicExponent=").append(publicExponent).append("\r\n");
		sb.append("modulus=").append(modulus).append("\r\n");
		out.write(sb.toString().getBytes("ISO-8859-1"));
	}

}
