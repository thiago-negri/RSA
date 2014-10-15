package rsa.math.sqrt;

import java.math.BigDecimal;
import java.math.BigInteger;

public class KarraSqrt implements SqrtMethod {


	//http://www.java-examples.com/find-square-root-biginteger-example
	public BigDecimal sqrt(BigInteger num){
		int firsttime = 0;
		BigDecimal myNumber = new BigDecimal(num);
		BigDecimal g = new BigDecimal("1");
		BigDecimal my2 = new BigDecimal("2");
		BigDecimal epsilon = new BigDecimal("0.0000000001");

		BigDecimal nByg = myNumber.divide(g, 9, BigDecimal.ROUND_FLOOR);

		//Get the value of n/g
		BigDecimal nBygPlusg = nByg.add(g);

		//Get the value of "n/g + g
		BigDecimal nBygPlusgHalf =  nBygPlusg.divide(my2, 9, BigDecimal.ROUND_FLOOR);

		//Get the value of (n/g + g)/2
		BigDecimal  saveg = nBygPlusgHalf;
		firsttime = 99;

		do {
			g = nBygPlusgHalf;
			nByg = myNumber.divide(g, 9, BigDecimal.ROUND_FLOOR);
			nBygPlusg = nByg.add(g);
			nBygPlusgHalf =  nBygPlusg.divide(my2, 9, BigDecimal.ROUND_FLOOR);
			BigDecimal  savegdiff  = saveg.subtract(nBygPlusgHalf);

			if (savegdiff.compareTo(epsilon) == -1 ) {
				firsttime = 0;
			} else {
				saveg = nBygPlusgHalf;
			}

		} while (firsttime > 1);

		return saveg;
	}
}
