package com.unimerch.repository.model.amzn_user;

public enum AzmnStatus {
    TERMINATED("TERMINATED"), APPROVED("APPROVED");

    private final String value;

    AzmnStatus(String value) {
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
