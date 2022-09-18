package com.unimerch.util;

public class NaturalSortString {
    public static int compareString(String s1, String s2) {
        if (s1.equalsIgnoreCase(s2))
            return 0;

        String[] tokens1 = s1.split(" ");
        String[] tokens2 = s2.split(" ");

        if (!tokens1[0].equalsIgnoreCase(tokens2[0]))
            return s1.compareToIgnoreCase(s2);

        int number1 = Integer.parseInt(tokens1[1].replaceAll("\\D", ""));
        int number2 = Integer.parseInt(tokens2[1].replaceAll("\\D", ""));

        if (number1 != number2)
            return number1 - number2;

        String suffix1 = tokens1[1].replaceAll("\\d", "");
        String suffix2 = tokens2[1].replaceAll("\\d", "");

        return suffix1.compareToIgnoreCase(suffix2);
    }
}
