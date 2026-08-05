package org.groomi.groomidevbackend.auth.dto.register;

import java.util.UUID;

public record RegisterResponse(UUID userId, String email) {

}
