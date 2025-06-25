package com.blockchain.csr.model.enums;

public enum EventType {
    OFFLINE("offline"),
    ONLINE("online"),
    HYBRID("hybrid");

    private final String value;

    EventType(String value) {
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