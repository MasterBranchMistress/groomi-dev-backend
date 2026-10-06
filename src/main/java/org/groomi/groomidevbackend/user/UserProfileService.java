package org.groomi.groomidevbackend.user;

import org.groomi.groomidevbackend.user.dto.load_user_profile.LoadUserProfileRequest;
import org.groomi.groomidevbackend.user.dto.load_user_profile.LoadUserProfileResponse;
import org.groomi.groomidevbackend.user.exception_handlers.load_user_profile.AccountDoesNotExistException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserRepository userRepository;

    public UserProfileService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }
    public LoadUserProfileResponse loadUserProfile(LoadUserProfileRequest request) {

        UserProfile user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AccountDoesNotExistException(request.getUserId()));

        return new LoadUserProfileResponse(user);
    }
}
