package Classes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Validations {

    // String Validations
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isValidLength(String input, int maxLength) {
        return input != null && input.length() <= maxLength;
    }

    // Email Validation
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Password Validation
    public static String getPasswordError(String password, int minLength) {
        if (password == null || password.length() < minLength) {
            return "Password must be at least " + minLength + " characters.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one number.";
        }
        if (!password.matches(".*[!@#$%^&*()].*")) {
            return "Password must contain at least one special character (!@#$%^&*()).";
        }
        return null; // Password is valid
    }

    // Date Validation
    public static LocalDate parseAndValidateDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString); // Requires YYYY-MM-DD
            if (date.isAfter(LocalDate.now())) {
                return null; // Date cannot be in the future
            }
            return date;
        } catch (DateTimeParseException _) {
            return null; // Invalid format
        }
    }
}