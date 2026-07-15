package org.groomi.groomidevbackend.auth.dto.register;

import lombok.Getter;

@Getter
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;


}
