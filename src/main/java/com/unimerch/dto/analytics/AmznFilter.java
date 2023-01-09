package com.unimerch.dto.analytics;

public enum AmznFilter {

    ALL("allAmzn"),
    GROUP("amznInGroup"),
    AMZN("amzn");

    private final String value;

    AmznFilter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AmznFilter parseDateFilter(String value) {
        AmznFilter[] values = values();
        for (AmznFilter status : values) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid parse " + value);
    }
}
