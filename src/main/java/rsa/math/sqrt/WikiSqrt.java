package rsa.math.sqrt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;

public class WikiSqrt  implements SqrtMethod {

	 public BigDecimal sqrt(final BigInteger num) {
	        // DON'T EVEN ASK OR TRY TO UNDERSTAND
	        // WIKIPEDIA FOR THE RESCUE
	        // - https://en.wikipedia.org/wiki/Methods_of_computing_square_roots
		 	BigDecimal bdnum = new BigDecimal(num);
		 	BigDecimal res = BigDecimal.ZERO;

	        int i = 2;

	        BitSet bitSet = new BitSet();
	        bitSet.set(i);

	        // "bit" starts at the highest power of four <= the argument.
	        BigDecimal bit = new BigDecimal(new BigInteger(bitSet.toByteArray()));
	        BigDecimal lastBit = bit;
	        while (bit.compareTo(bdnum) < 0) {
	            lastBit = bit;
	            bit = bit.multiply(BigDecimal.valueOf(4L));
	        }

	        bit = lastBit;

	        while (!bit.equals(BigInteger.ZERO)) {
	        	BigDecimal resPlusBit = res.add(bit);
	            if (bdnum.compareTo(resPlusBit) >= 0) {
	            	bdnum = bdnum.subtract(resPlusBit);
	                res = (res.divide(BigDecimal.valueOf(2))).add(bit);
	            } else {
	                res = res.divide(BigDecimal.valueOf(2));
	            }
	            bit = bit.divide(BigDecimal.valueOf(4));
	        }
	        return res;
	    }

}
