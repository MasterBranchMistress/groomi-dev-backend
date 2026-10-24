package org.groomi.groomidevbackend.metadata.pet_owners.exception_handlers;

public class PetOwnerDoesNotExistException extends RuntimeException {
    public PetOwnerDoesNotExistException() {
        super("This Pet Owner does not exist, please try again later.");
    }
}
