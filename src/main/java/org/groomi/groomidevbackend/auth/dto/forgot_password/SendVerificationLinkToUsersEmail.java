package org.groomi.groomidevbackend.auth.dto.forgot_password;

import lombok.Getter;
import lombok.Setter;

public class SendVerificationLinkToUsersEmail {
    @Getter
    @Setter
    private String email;
}
