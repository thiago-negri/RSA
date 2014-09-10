package rsa.api;

import java.io.InputStream;

@FunctionalInterface
public interface BigIntegerStreamConstructor {

	BigIntegerStream build(InputStream stream);

}
