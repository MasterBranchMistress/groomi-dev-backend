package org.groomi.groomidevbackend.metadata.pet_owners;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetOwnerRepository extends JpaRepository<PetOwner, UUID> {
    default boolean isOwnersDesignatedGroomer(UUID groomerId, UUID loggedInUserId){
        return groomerId.toString().equals(loggedInUserId.toString());
    }
}
