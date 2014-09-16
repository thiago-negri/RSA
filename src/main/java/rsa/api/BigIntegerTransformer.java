package rsa.api;

import java.math.BigInteger;

@FunctionalInterface
public interface BigIntegerTransformer<E extends Throwable> {

    BigInteger transform(BigInteger input) throws E;

}
