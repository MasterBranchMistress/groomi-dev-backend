package org.groomi.groomidevbackend.metadata.pet_owners;



import jakarta.validation.Valid;

import org.groomi.groomidevbackend.metadata.pet_owners.dto.create_pet_owner.CreatePetOwnerRequest;
import org.groomi.groomidevbackend.metadata.pet_owners.dto.create_pet_owner.CreatePetOwnerResponse;
import org.groomi.groomidevbackend.metadata.pet_owners.dto.read_pet_owner.ReadPetOwnerRequest;
import org.groomi.groomidevbackend.metadata.pet_owners.dto.read_pet_owner.ReadPetOwnerResponse;
import org.groomi.groomidevbackend.shared_packages.api_response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pet-owner")
public class PetOwnerController {

    private final PetOwnerService PetOwnerService;

    public PetOwnerController(PetOwnerService PetOwnerService) {
        this.PetOwnerService = PetOwnerService;
    }

    @PostMapping("/read-pet-owner")
    public ResponseEntity<ApiResponse<ReadPetOwnerResponse>> readPetOwner(
            @Valid @RequestBody ReadPetOwnerRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        "Successfully retrieved owner.",
                        PetOwnerService.readPetOwner(request)
                ));
    }

    @PostMapping("/create-pet-owner")
    public ResponseEntity<ApiResponse<CreatePetOwnerResponse>> createPetOwner(
            @Valid @RequestBody CreatePetOwnerRequest request
            ){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                "Successfully created new pet owner.",
                PetOwnerService.createPetOwner(request)
        ));
    }
}
