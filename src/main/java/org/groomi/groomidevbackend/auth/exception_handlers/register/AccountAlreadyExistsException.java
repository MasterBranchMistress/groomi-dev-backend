package org.groomi.groomidevbackend.auth.exception_handlers.register;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(String email){
        super("Account already exists for " + email + ". Please try logging in.");
    }
}
