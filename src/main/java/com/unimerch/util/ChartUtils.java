package com.unimerch.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ChartUtils {
    public long getMaxAxis(BigDecimal num) {
        int scale = -1;

        int numberDigits = getNumberDigits(num);

        if (numberDigits > 3)
            scale -= numberDigits - 3;

        num = num.setScale(scale, RoundingMode.CEILING);

        num = beautifyMaxAxis(num, numberDigits);

        return num.longValue();
    }

    public int getNumberDigits(BigDecimal n) {
        return n.signum() == 0 ? 1 : n.precision() - n.scale();
    }

    public BigDecimal beautifyMaxAxis(BigDecimal num, int numberDigits) {
        int ten = 10;
        BigDecimal divisor = BigDecimal.valueOf(Math.pow(ten, numberDigits - 2));

        BigDecimal firstTwoDigits = num.divide(divisor);

        if (!firstTwoDigits.remainder(BigDecimal.valueOf(2)).equals(BigDecimal.ZERO))
            firstTwoDigits = firstTwoDigits.add(BigDecimal.ONE);

        num = firstTwoDigits.multiply(divisor);

        if (num.compareTo(BigDecimal.ZERO) == 0)
            num = num.add(BigDecimal.TEN);

        return num;
    }
}
