package org.groomi.groomidevbackend.dashboard.dto.load_user_dashboard;

import java.util.UUID;

public record LoadUserDashboardResponse(
        String firstName,
        String lastName,
        String email
) {
}