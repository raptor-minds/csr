package com.blockchain.csr.model.enums;

public enum ActivityStatus {
    NOT_STARTED("NOT_STARTED"),
    IN_PROGRESS("IN_PROGRESS"),
    FINISHED("FINISHED");

    private final String value;

    ActivityStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
} 