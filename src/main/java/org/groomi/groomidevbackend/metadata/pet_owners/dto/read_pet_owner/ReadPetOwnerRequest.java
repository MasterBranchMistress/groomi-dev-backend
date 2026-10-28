package org.groomi.groomidevbackend.metadata.pet_owners.dto.read_pet_owner;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReadPetOwnerRequest {
    private  UUID ownerId;
    private String groomerToken;
}
