package org.groomi.groomidevbackend.auth.exception_handlers.login;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String email, String password){
        super("Credentials Invalid. Please check your email or password and try again.");
    }
}
