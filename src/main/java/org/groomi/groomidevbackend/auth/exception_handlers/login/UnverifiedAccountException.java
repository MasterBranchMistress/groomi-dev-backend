package org.groomi.groomidevbackend.auth.exception_handlers.login;

public class UnverifiedAccountException extends RuntimeException {
    public UnverifiedAccountException(Boolean accountVerified){
        super("Please check your email and verify your account before logging in.");
    }
}
