package rsa.api;

import java.math.BigInteger;
import java.util.Optional;

public interface BigIntegerStream<E extends Throwable> {

    Optional<BigInteger> next() throws E;

    boolean hasNext() throws E;

}
