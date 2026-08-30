package org.groomi.groomidevbackend.auth.dto.change_password;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String token;
    private String newPassword;
}
