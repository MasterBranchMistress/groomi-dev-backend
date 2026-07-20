package org.groomi.groomidevbackend.auth.dto.login;

import java.util.UUID;

public record LoginResponse(UUID userId, String email, String token) {

}
