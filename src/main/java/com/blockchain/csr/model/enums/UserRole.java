package com.blockchain.csr.model.enums;

/**
 * User role enumeration
 * 
 * @author system
 */
public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.value.equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
} 