package org.groomi.groomidevbackend.auth.dto.logout;

import lombok.Getter;

import java.util.UUID;

@Getter
public class LogoutResponse {
    private String message;

    public LogoutResponse(String message) {
        this.message = message;
    }

}
