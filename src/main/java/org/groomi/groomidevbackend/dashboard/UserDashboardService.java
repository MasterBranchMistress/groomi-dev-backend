package org.groomi.groomidevbackend.dashboard;

import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;
import org.groomi.groomidevbackend.dashboard.dto.load_user_dashboard.LoadUserDashboardResponse;
import org.groomi.groomidevbackend.dashboard.exception_handlers.load_user_profile.AccountDoesNotExistException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDashboardService {

    private final UserDashboardRepository userDashboardRepository;
    private final JwtService jwtService;

    public UserDashboardService(
            UserDashboardRepository userDashboardRepository, JwtService jwtService
    ) {
        this.userDashboardRepository = userDashboardRepository;
        this.jwtService = jwtService;
    }
    public LoadUserDashboardResponse loadUserDashboard(String token) {
        UUID userId =  jwtService.extractUserId(token);
        UserDashboard userDashboardData = userDashboardRepository.findById(userId)
                .orElseThrow(AccountDoesNotExistException::new);

        return new LoadUserDashboardResponse(userDashboardData.getFirstName(), userDashboardData.getLastName(), userDashboardData.getEmail());
    }
}
