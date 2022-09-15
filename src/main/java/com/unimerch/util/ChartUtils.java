package com.unimerch.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ChartUtils {
    public static long getMaxAxis(BigDecimal num) {
        int scale = -1;

        int numberDigits = getNumberDigits(num);

        if (numberDigits > 3)
            scale -= numberDigits - 3;

        num = num.setScale(scale, RoundingMode.CEILING);

        num = beautifyMaxAxis(num, numberDigits);

        return num.longValue();
    }

    public static int getNumberDigits(BigDecimal n) {
        return n.signum() == 0 ? 1 : n.precision() - n.scale();
    }

    public static BigDecimal beautifyMaxAxis(BigDecimal num, int numberDigits) {
        if (num.compareTo(BigDecimal.ZERO) == 0) {
            num = num.add(BigDecimal.TEN);
            return num;
        }

        int ten = 10;
        BigDecimal divisor = BigDecimal.valueOf(Math.pow(ten, numberDigits - 2));
        BigDecimal firstTwoDigits = num.divide(divisor);

        if (!firstTwoDigits.remainder(BigDecimal.valueOf(2)).equals(BigDecimal.ZERO))
            firstTwoDigits = firstTwoDigits.add(BigDecimal.ONE);

        num = firstTwoDigits.multiply(divisor);

        return num;
    }
}
