package com.campushub.util;

public class PasswordValidator {

    private static final int MIN_LENGTH = 8;

    public static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }

        if (password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Password must be at least " + MIN_LENGTH + " characters long");
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        if (!hasLetter || !hasDigit) {
            throw new IllegalArgumentException("Password must contain both letters and digits");
        }
    }
}
