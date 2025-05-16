package com.swaglabs.enums;

/**
 * Enumeration of user types available in Swag Labs
 * Each user type represents a different user experience
 */
public enum UserType {
    STANDARD_USER("standard_user"),
    LOCKED_OUT_USER("locked_out_user"),
    PROBLEM_USER("problem_user"),
    PERFORMANCE_GLITCH_USER("performance_glitch_user"),
    ERROR_USER("error_user"),
    VISUAL_USER("visual_user");
    
    private final String username;
    private static final String PASSWORD = "secret_sauce";
    
    /**
     * Constructor for UserType enum
     * @param username The username for this user type
     */
    UserType(String username) {
        this.username = username;
    }
    
    /**
     * Get the username for this user type
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Get the password for all user types
     * @return The password
     */
    public String getPassword() {
        return PASSWORD;
    }
    
    /**
     * Return string representation of user type
     * @return Username string
     */
    @Override
    public String toString() {
        return username;
    }
}
