package com.carpooling.carpooling.dto;


import jakarta.validation.constraints.NotEmpty;
import org.antlr.v4.runtime.misc.NotNull;

public class LoginDto {

    @NotEmpty(message = "Username can't be empty")
    private String email;

    @NotEmpty(message = "Password can't be empty")
    private String password;

    public LoginDto(){

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
}
