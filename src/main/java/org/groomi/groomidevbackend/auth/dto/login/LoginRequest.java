package org.groomi.groomidevbackend.auth.dto.login;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private  String password;

    public void setEmail(String email) {
    }
    public void setPassword(String email) {
    }
}
