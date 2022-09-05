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

        return num.longValue();
    }

    public int getNumberDigits(BigDecimal n) {
        return n.signum() == 0 ? 1 : n.precision() - n.scale();
    }
}
