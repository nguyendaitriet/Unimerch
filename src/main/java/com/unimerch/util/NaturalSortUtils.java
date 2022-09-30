package com.unimerch.util;

public class NaturalSortUtils {
    public static int compareString(String o1, String o2) {
        String o1StringPart = o1.replaceAll("\\d", "");
        String o2StringPart = o2.replaceAll("\\d", "");

        if(o1StringPart.equalsIgnoreCase(o2StringPart))
        {
//            return (int) (extractInt(o1) - extractInt(o2));
            return Long.compare(extractNumber(o1), extractNumber(o2));
        }
        return o1.compareTo(o2);
    }

    public static long extractNumber(String s) {
        String num = s.replaceAll("\\D", "");
        return num.isEmpty() ? 0 : Long.parseLong(num);
    }

}
