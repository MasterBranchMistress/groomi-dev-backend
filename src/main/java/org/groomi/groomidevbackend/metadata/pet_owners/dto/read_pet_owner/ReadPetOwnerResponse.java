package org.groomi.groomidevbackend.metadata.pet_owners.dto.read_pet_owner;

import java.util.UUID;

public record ReadPetOwnerResponse(String firstName,
                                   String lastName,
                                   String phoneNumber,
                                   String email,
                                   String petOwnerPhotoUrl,
                                   UUID groomerId) {
}
