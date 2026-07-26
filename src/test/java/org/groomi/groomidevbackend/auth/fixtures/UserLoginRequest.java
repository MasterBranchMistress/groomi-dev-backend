package org.groomi.groomidevbackend.auth.fixtures;

import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;

public class UserLoginRequest {
        public static LoginRequest isValidLoginRequest() {
            LoginRequest request = new LoginRequest();
            request.setEmail("jimmie@example.com");
            request.setPassword("Password123!");
            return request;
        }
    public static LoginRequest hasInvalidEmail() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong_email@example.com");
        request.setPassword("Password123!");
        return request;
    }
    public static LoginRequest hasInvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setEmail("jimmie@example.com");
        request.setPassword("WrongPassword123!");
        return request;
    }
}
