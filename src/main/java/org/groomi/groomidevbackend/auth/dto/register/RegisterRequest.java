package org.groomi.groomidevbackend.auth.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class RegisterRequest {

    @Setter
    @NotBlank
    private String firstName;
    @Setter
    @NotBlank
    private String lastName;
    @Setter
    @NotBlank
    private String phoneNumber;
    @Setter
    @Email
    private String email;
    @Setter
    @Size(min = 8)
    private String password;
}
