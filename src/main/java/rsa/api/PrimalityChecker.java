package rsa.api;

import java.math.BigInteger;
import java.util.function.Predicate;

@FunctionalInterface
public interface PrimalityChecker extends Predicate<BigInteger> {

}
