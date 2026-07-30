package org.groomi.groomidevbackend.auth.dto.forgot_password;

import lombok.Getter;
import lombok.Setter;

public class ForgotPasswordRequest {
    @Getter
    @Setter
    private String email;
}
