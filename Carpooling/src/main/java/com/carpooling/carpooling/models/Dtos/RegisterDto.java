package com.carpooling.carpooling.models.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDto {

    @NotBlank(message = "Password is required.")
    @Size(min =8, message = "Password must be at least 8 characters.")
    private String password;

    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirm;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    private String lastName;

    @NotEmpty(message = "Username can't be empty")
    private String username;

    @NotEmpty(message = "Email is needed")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be correct")
    private String phoneNumber;

    public @NotEmpty(message = "Phone number is required") @Pattern(regexp = "\\d{10}", message = "Phone number must be correct") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotEmpty(message = "Phone number is required") @Pattern(regexp = "\\d{10}", message = "Phone number must be correct") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Password is required.") @Size(min = 8, message = "Password must be at least 8 characters.") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required.") @Size(min = 8, message = "Password must be at least 8 characters.") String password) {
        this.password = password;
    }

    public @NotEmpty(message = "Email is needed") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "Email is needed") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "Password confirmation can't be empty") String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(@NotEmpty(message = "Password confirmation can't be empty") String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public @NotEmpty(message = "First name can't be empty") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotEmpty(message = "First name can't be empty") String firstName) {
        this.firstName = firstName;
    }

    public @NotEmpty(message = "Last name can't be empty") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotEmpty(message = "Last name can't be empty") String lastName) {
        this.lastName = lastName;
    }

    public @NotEmpty(message = "Username can't be empty") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "Username can't be empty") String username) {
        this.username = username;
    }
}
