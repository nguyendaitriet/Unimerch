package com.unimerch.util;

import com.unimerch.exception.ServerErrorException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TimeUtils {
    static final public ZoneId zoneIdVN = ZoneId.of("Asia/Ho_Chi_Minh");

    static final private String dayMonthYearPattern = "dd/MM/yyyy";

    static final private String monthYearPattern = "MM/yyyy";

    static final private String hoursFormat = "%d hour(s) ago";

    static final private String daysAndHoursFormat = "%d day(s) ago";


    public static Instant parseStringToInstant(String date) {
        return ZonedDateTime.parse(date).toInstant();
    }

    public static String toDayMonthYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(dayMonthYearPattern));
    }

    public static String toMonthYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(monthYearPattern));
    }

    public static Instant getInstantToday() {
        LocalDate today = LocalDate.now();
        ZonedDateTime zdtToday = today.atStartOfDay(zoneIdVN);
        return zdtToday.toInstant();
    }

    public static String getCardTimeToday() {
        LocalDate today = LocalDate.now();
        return toDayMonthYear(today);
    }

    public static Map<String, Instant> getInstantYesterday() {
        Map<String, Instant> results = new HashMap<>();
        LocalDate today = LocalDate.now();
        LocalDate localYesterday = today.minusDays(1);
        ZonedDateTime zdtYesterdayStart = localYesterday.atStartOfDay(zoneIdVN);
        Instant startTime = zdtYesterdayStart.toInstant();
        results.put("startTime", startTime);

        ZonedDateTime zdtYesterdayEnd = today.atStartOfDay(zoneIdVN);
        Instant endTime = zdtYesterdayEnd.toInstant();
        results.put("endTime", endTime);

        return results;
    }

    public static String getCardTimeYesterday() {
        LocalDate localYesterday = LocalDate.now().minusDays(1);
        return toDayMonthYear(localYesterday);
    }

    public static Instant getInstantLastSomeDays(int days) {
        LocalDate lastSevenDay = LocalDate.now().minusDays(days - 1);
        ZonedDateTime zdtLastWeek = lastSevenDay.atStartOfDay(zoneIdVN);

        return zdtLastWeek.toInstant();
    }

    public static String getCardTimeLastSevenDays() {
        LocalDate lastSevenDay = LocalDate.now().minusDays(6);
        return toMonthYear(lastSevenDay);
    }

    public static Instant getInstantThisMonth() {
        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        ZonedDateTime zdtThisMonth = firstDayOfThisMonth.atStartOfDay(zoneIdVN);

        return zdtThisMonth.toInstant();
    }

    public static String getCardTimeThisMonth() {
        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        return toMonthYear(firstDayOfThisMonth);
    }

    public static Map<String, Instant> getInstantPreviousMonth() {
        Map<String, Instant> results = new HashMap<>();

        LocalDate firstDayOfLastMonth = YearMonth.now().minusMonths(1).atDay(1);
        ZonedDateTime zdtFirstDayofLastMonth = firstDayOfLastMonth.atStartOfDay(zoneIdVN);
        Instant startTime = zdtFirstDayofLastMonth.toInstant();
        results.put("startTime", startTime);

        LocalDate firstDayOfThisMonth = LocalDate.now().withDayOfMonth(1);
        ZonedDateTime zdtFirstDayOfThisMonth = firstDayOfThisMonth.atStartOfDay(zoneIdVN);
        Instant endTime = zdtFirstDayOfThisMonth.toInstant();
        results.put("endTime", endTime);

        return results;
    }

    public static String getCardTimePreviousMonth() {
        LocalDate firstDayOfLastMonth = YearMonth.now().minusMonths(1).atDay(1);
        return toMonthYear(firstDayOfLastMonth);
    }

    public static List<String> getCardsLastSevenDays() {
        List<String> cards = new ArrayList<>();

        LocalDate start = LocalDate.now().minusDays(6);

        IntStream.range(0, 7).mapToObj(start::plusDays).collect(Collectors.toList()).forEach(localDate -> cards.add(toDayMonthYear(localDate)));

        return cards;
    }

    public static Date instantToDateNoTime(Instant instant) {
        return instantToDate(instant, "E MMM dd yyyy");
    }

    public static Date instantToDate(Instant instant, String pattern) {
        String dateString = Date.from(instant).toString();
        String dateNoTimeString = dateString.substring(0, 10) + dateString.substring(23, 28);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(dateNoTimeString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static LocalDate instantToLocalDate(Instant instant) {
        return instant.atZone(zoneIdVN).toLocalDate();
    }

    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static Instant getInstantSomeHourBefore(int hours) {
        return Instant.now().minus(hours, ChronoUnit.HOURS);
    }

    public static String getDurationBetween(Instant start, Instant end) {
        Duration duration = Duration.between(start, end);
        int durationInHour = (int) Math.abs(duration.toHours());

        if (durationInHour <= 24)
            return String.format(hoursFormat, durationInHour);

        int days = durationInHour / 24;

        return String.format(daysAndHoursFormat, days);
    }

    public static void main(String[] args) {
        System.out.println(getInstantToday());;
    }
}
