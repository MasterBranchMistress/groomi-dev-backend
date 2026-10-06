package org.groomi.groomidevbackend.user.dto.load_user_profile;

import lombok.Getter;
import lombok.Setter;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;

import java.util.UUID;

@Getter
@Setter
public class LoadUserProfileRequest {
    private UUID userId;
    private TokenType validLoginToken;
}