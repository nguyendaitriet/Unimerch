package com.unimerch.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChartUtils {
    public static long getMaxAxis(BigDecimal num) {
        if (num.compareTo(BigDecimal.ZERO) <= 0)
            return num.add(BigDecimal.TEN).longValue();

        int scale = -1;
        int numberDigits = getNumberDigits(num);

        if (numberDigits > 3)
            scale -= numberDigits - 3;

        BigDecimal result = num.setScale(scale, RoundingMode.CEILING);

        BigDecimal divisor = BigDecimal.valueOf(Math.pow(10, numberDigits - 2));
        BigDecimal firstTwoDigits = result.divide(divisor);

        if (result.compareTo(num) == 0) {
            if (numberDigits > 3) {
                firstTwoDigits = firstTwoDigits.add(BigDecimal.ONE);
            } else {
                firstTwoDigits = firstTwoDigits.add(BigDecimal.TEN);
            }
        }

        result = firstTwoDigits.multiply(divisor);

        return result.longValue();
    }

    public static int getNumberDigits(BigDecimal n) {
        return n.signum() == 0 ? 1 : n.precision() - n.scale();
    }
}
