package org.groomi.groomidevbackend.dashboard.dto.load_user_dashboard;

import lombok.Getter;
import lombok.Setter;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;

import java.util.UUID;

@Getter
@Setter
public class LoadUserDashboardRequest {
    private UUID userId;
}