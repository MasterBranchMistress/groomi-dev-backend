package org.groomi.groomidevbackend.auth.dto.login;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LoginResponse {
    private UUID userId;
    private String email;
    private String message;

    public LoginResponse(UUID userId, String email, String message) {
        this.userId =  userId;
        this.email =  email;
        this.message = message;
    }

}
