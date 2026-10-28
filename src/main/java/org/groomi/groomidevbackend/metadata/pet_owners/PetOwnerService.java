package org.groomi.groomidevbackend.metadata.pet_owners;

import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;
import org.groomi.groomidevbackend.dashboard.UserDashboard;
import org.groomi.groomidevbackend.dashboard.UserDashboardRepository;
import org.groomi.groomidevbackend.metadata.pet_owners.dto.create_pet_owner.CreatePetOwnerRequest;
import org.groomi.groomidevbackend.metadata.pet_owners.dto.create_pet_owner.CreatePetOwnerResponse;
import org.groomi.groomidevbackend.metadata.pet_owners.dto.read_pet_owner.ReadPetOwnerRequest;
import org.groomi.groomidevbackend.metadata.pet_owners.dto.read_pet_owner.ReadPetOwnerResponse;
import org.groomi.groomidevbackend.metadata.pet_owners.exception_handlers.PetOwnerDoesNotExistException;
import org.groomi.groomidevbackend.metadata.pet_owners.exception_handlers.UnauthorizedGroomerAction_OWNERS;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PetOwnerService {

    private final UserDashboardRepository userDashboardRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final JwtService jwtService;

    public PetOwnerService(
            PetOwnerRepository petOwnerRepository, JwtService jwtService, UserDashboardRepository userDashboardRepository
    ) {
        this.petOwnerRepository = petOwnerRepository;
        this.jwtService = jwtService;
        this.userDashboardRepository =  userDashboardRepository;
    }
    public ReadPetOwnerResponse readPetOwner(ReadPetOwnerRequest request) {
        UUID groomerId =  jwtService.extractUserId(request.getGroomerToken());
        PetOwner petOwnerData = petOwnerRepository.findById(request.getOwnerId())
                .orElseThrow(PetOwnerDoesNotExistException::new);
        boolean isNotAuthorized = !petOwnerRepository.isOwnersDesignatedGroomer(petOwnerData.getGroomerId(), groomerId);
        if(isNotAuthorized) throw new UnauthorizedGroomerAction_OWNERS("You do not have permission to view this pet owner's data." + petOwnerData.getGroomerId().toString() + " is not equal to " + groomerId);
        return new ReadPetOwnerResponse(petOwnerData.getFirstName(), petOwnerData.getLastName(), petOwnerData.getPhoneNumber()
        , petOwnerData.getEmail(), petOwnerData.getOwnerPhotoUrl(), petOwnerData.getGroomerId());
    }

    public CreatePetOwnerResponse createPetOwner(CreatePetOwnerRequest request){
        UUID userId =  jwtService.extractUserId(request.getGroomerToken());
        UserDashboard user = userDashboardRepository.getReferenceById(userId);
        boolean userDoesNotExistOrIsInvalid = user.getId().toString().isEmpty();
        PetOwner petOwner =  new PetOwner(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber(), request.getPetOwnerPhotoUrl(), userId);
        if(userDoesNotExistOrIsInvalid) throw new UnauthorizedGroomerAction_OWNERS("you do not have permission to create a pet owner.");
        petOwnerRepository.save(petOwner);
        return new CreatePetOwnerResponse("Successfully created new pet owner.");
    }
}

