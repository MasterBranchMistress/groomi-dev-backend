package org.groomi.groomidevbackend.auth.dto.verify_account.register;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyNewAccountRequest {
    String token;
}
