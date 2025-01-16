package com.carpooling.carpooling.dto;

import jakarta.validation.constraints.NotEmpty;

public class RegisterDto extends LoginDto{

    @NotEmpty(message = "Password confirmation can't be empty")
    private String passwordConfirm;

    @NotEmpty(message = "First name can't be empty")
    private String firstName;

    @NotEmpty(message = "Last name can't be empty")
    private String lastName;

    @NotEmpty(message = "Username can't be empty")
    private String username;

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
