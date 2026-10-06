package org.groomi.groomidevbackend.user;

import jakarta.validation.Valid;

import org.groomi.groomidevbackend.shared_packages.api_response.ApiResponse;
import org.groomi.groomidevbackend.user.dto.load_user_profile.LoadUserProfileRequest;
import org.groomi.groomidevbackend.user.dto.load_user_profile.LoadUserProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private final UserProfileService UserProfileService;

    public UserProfileController(UserProfileService UserProfileService) {
        this.UserProfileService = UserProfileService;
    }

    @PostMapping("/load-user-profile")
    public ResponseEntity<ApiResponse<LoadUserProfileResponse>> loadUserProfile(
    @Valid @RequestBody
    LoadUserProfileRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        "{success message}",
                        UserProfileService.loadUserProfile(request)
                ));
    }
}
