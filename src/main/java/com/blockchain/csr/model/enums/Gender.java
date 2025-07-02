package com.blockchain.csr.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gender enumeration for user profile
 */
public enum Gender {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }
        for (Gender gender : Gender.values()) {
            if (gender.value.equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender value: " + value + ". Must be one of: male, female, other");
    }

    @Override
    public String toString() {
        return value;
    }
} 