package com.blockchain.csr.config;

import com.blockchain.csr.model.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * Security utility class for role and permission checks
 */
public class SecurityUtils {

    /**
     * Get the current authenticated user's username
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Get the current authenticated user's role
     */
    public static UserRole getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                if (role.startsWith("ROLE_")) {
                    String roleValue = role.substring(5); // Remove "ROLE_" prefix
                    try {
                        return UserRole.fromString(roleValue);
                    } catch (IllegalArgumentException e) {
                        // Continue to next authority if invalid
                    }
                }
            }
        }
        return UserRole.USER; // Default to USER if no valid role found
    }

    /**
     * Check if the current user has the specified role
     */
    public static boolean hasRole(UserRole role) {
        return getCurrentUserRole() == role;
    }

    /**
     * Check if the current user has any of the specified roles
     */
    public static boolean hasAnyRole(UserRole... roles) {
        UserRole currentRole = getCurrentUserRole();
        for (UserRole role : roles) {
            if (currentRole == role) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the current user is an admin
     */
    public static boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }

    /**
     * Check if the current user is authenticated
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getName());
    }

    /**
     * Throw exception if current user doesn't have required role
     */
    public static void requireRole(UserRole role) {
        if (!hasRole(role)) {
            throw new SecurityException("Access denied. Required role: " + role);
        }
    }

    /**
     * Throw exception if current user doesn't have any of the required roles
     */
    public static void requireAnyRole(UserRole... roles) {
        if (!hasAnyRole(roles)) {
            StringBuilder sb = new StringBuilder("Access denied. Required roles: ");
            for (int i = 0; i < roles.length; i++) {
                sb.append(roles[i]);
                if (i < roles.length - 1) {
                    sb.append(", ");
                }
            }
            throw new SecurityException(sb.toString());
        }
    }
} 