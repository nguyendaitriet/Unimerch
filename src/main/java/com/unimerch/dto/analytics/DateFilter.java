package com.unimerch.dto.analytics;

public enum DateFilter {
    TODAY("today"),
    YESTERDAY("yesterday"),
    PREVIOUS_MONTH("previousMonth"),
    THIS_MONTH("thisMonth"),
    CUSTOM("custom");

    private final String value;

    DateFilter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DateFilter parseDateFilter(String value) {
        DateFilter[] values = values();
        for (DateFilter status : values) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid parse " + value);
    }
}
