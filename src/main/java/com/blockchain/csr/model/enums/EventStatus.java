package com.blockchain.csr.model.enums;

public enum EventStatus {
    ACTIVE("active"),
    ENDED("ended");

    private final String value;

    EventStatus(String value) {
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