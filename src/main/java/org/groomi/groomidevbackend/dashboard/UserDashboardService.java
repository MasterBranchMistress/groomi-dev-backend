package org.groomi.groomidevbackend.dashboard;

import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;
import org.groomi.groomidevbackend.dashboard.dto.load_user_dashboard.LoadUserDashboardResponse;
import org.groomi.groomidevbackend.dashboard.exception_handlers.load_user_profile.AccountDoesNotExistException;
import org.springframework.stereotype.Service;

@Service
public class UserDashboardService {

    private final UserDashboardRepository userDashboardRepository;

    public UserDashboardService(
            UserDashboardRepository userDashboardRepository
    ) {
        this.userDashboardRepository = userDashboardRepository;
    }
    public LoadUserDashboardResponse loadUserDashboard(JwtService jwtService, String token) {

        var userToken =  jwtService.extractAllClaims(token);

        UserDashboard userDashboardData = userDashboardRepository.findById(jwtService.extractUserId(userToken.getId()))
                .orElseThrow(AccountDoesNotExistException::new);

        return new LoadUserDashboardResponse(userDashboardData.getFirstName(), userDashboardData.getLastName(), userDashboardData.getEmail());
    }
}
