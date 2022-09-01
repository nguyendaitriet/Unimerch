package com.unimerch.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtils {
    final public ZoneId zoneIdVN = ZoneId.of("Asia/Ho_Chi_Minh");

    final private String dayMonthYearPattern = "dd/MM/yyyy";

    final private String monthYearPattern = "MM/yyyy";

    public String toDayMonthYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(dayMonthYearPattern));
    }

    public String toMonthYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(monthYearPattern));
    }


}
