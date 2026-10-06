package org.groomi.groomidevbackend.dashboard.exception_handlers.load_user_profile;

import java.util.UUID;

public class AccountDoesNotExistException extends RuntimeException{
    public AccountDoesNotExistException(){
        super("Oops. Something went wrong here. Please try again.");
    }
}
