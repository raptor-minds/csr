package com.blockchain.csr.model.enums;

/**
 * User activity state enumeration
 * 
 * @author system
 */
public enum UserActivityState {
    SIGNED_UP("SIGNED_UP"),
    WITHDRAWN("WITHDRAWN");

    private final String value;

    UserActivityState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static UserActivityState fromString(String state) {
        for (UserActivityState userActivityState : UserActivityState.values()) {
            if (userActivityState.value.equalsIgnoreCase(state)) {
                return userActivityState;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + state);
    }
} 