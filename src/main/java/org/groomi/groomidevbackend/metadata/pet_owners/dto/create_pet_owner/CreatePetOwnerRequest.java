package org.groomi.groomidevbackend.metadata.pet_owners.dto.create_pet_owner;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePetOwnerRequest {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String petOwnerPhotoUrl;
    UUID groomerId;
}
