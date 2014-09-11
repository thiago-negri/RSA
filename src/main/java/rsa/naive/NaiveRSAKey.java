package rsa.naive;

import rsa.api.Key;
import rsa.api.RSAKey;

public final class NaiveRSAKey implements RSAKey {

	private final Key privateKey;
	private final Key publicKey;

	public NaiveRSAKey(Key privateKey, Key publicKey) {
		if (!privateKey.modulus().equals(publicKey.modulus())) throw new IllegalArgumentException();
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}

	@Override
	public Key privateKey() {
		return privateKey;
	}

	@Override
	public Key publicKey() {
		return publicKey;
	}

}
