package Classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final String userID;
    private String firstName;
    private String surname;
    private LocalDate dateOfBirth;
    private String email;
    private String password;
    private boolean locked;
    private LocalDateTime lastLogin;
    private int failedLoginAttempts;

    public User(String firstName, String surname, LocalDate dateOfBirth, String email, String password) {
        this.userID = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.locked = false;
        this.failedLoginAttempts = 0;
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    // Logic
    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.locked = true;
        }
    }

    public void resetFailedLogin() {
        this.failedLoginAttempts = 0;
    }

    @Override
    public String toString() {
        return firstName + " " + surname + " (" + email + ")";
    }
}