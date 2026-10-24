package org.groomi.groomidevbackend.metadata.pet_owners;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetOwnerRepository extends JpaRepository<PetOwner, UUID> {
    default boolean isNotAuthorizedToDoAction(UUID groomerId, UUID petOwnerID){
        return groomerId != petOwnerID;
    }
}
