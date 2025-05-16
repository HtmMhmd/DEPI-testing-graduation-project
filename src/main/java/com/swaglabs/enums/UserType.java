package com.swaglabs.enums;

public enum UserType {
    STANDARD_USER("standard_user", "secret_sauce"),
    LOCKED_OUT_USER("locked_out_user", "secret_sauce"),
    PROBLEM_USER("problem_user", "secret_sauce"),
    PERFORMANCE_GLITCH_USER("performance_glitch_user", "secret_sauce"),
    ERROR_USER("error_user", "secret_sauce"),
    VISUAL_USER("visual_user", "secret_sauce");

    private final String username;
    private final String password;

    UserType(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
