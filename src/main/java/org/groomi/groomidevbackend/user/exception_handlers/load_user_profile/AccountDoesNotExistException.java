package org.groomi.groomidevbackend.user.exception_handlers.load_user_profile;

import org.groomi.groomidevbackend.user.UserProfile;

import java.util.UUID;

public class AccountDoesNotExistException extends RuntimeException{
    public AccountDoesNotExistException(UUID id){
        super("Oops. Something went wrong here. Please try again.");
    }
}
