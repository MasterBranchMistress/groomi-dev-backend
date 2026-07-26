package org.groomi.groomidevbackend.auth.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
    @Pattern(
            regexp = "^\\+?[0-9\\s\\-()]{10,20}$",
            message = "Invalid Phone Number."
    )
    private String phoneNumber;
    @Setter
    @Email(message = "Invalid Email Address.")
    private String email;
    @Setter
    @Size(min = 6)
    private String password;
}
