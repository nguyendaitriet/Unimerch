package com.unimerch.repository.model;

public enum AzmnStatus {
    TERMINATED("TERMINATED"), APPROVED("APPROVED");

    private final String value;

    private AzmnStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AzmnStatus parseAzmnStatus(String value) {
        AzmnStatus[] values = values();
        for (AzmnStatus status : values) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("invalid parse " + value);
    }
}
