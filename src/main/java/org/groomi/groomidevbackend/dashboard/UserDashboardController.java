package org.groomi.groomidevbackend.dashboard;

import jakarta.validation.Valid;

import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.dashboard.dto.load_user_dashboard.LoadUserDashboardRequest;
import org.groomi.groomidevbackend.shared_packages.api_response.ApiResponse;
import org.groomi.groomidevbackend.dashboard.dto.load_user_dashboard.LoadUserDashboardResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserDashboardController {

    private final UserDashboardService UserDashboardService;

    public UserDashboardController(UserDashboardService UserDashboardService) {
        this.UserDashboardService = UserDashboardService;
    }

    @PostMapping("/dashboard")
    public ResponseEntity<ApiResponse<LoadUserDashboardResponse>> loadUserDashboard(
    @Valid @RequestBody
    LoadUserDashboardRequest request
    ) {
        LoadUserDashboardResponse user =  UserDashboardService.loadUserDashboard(request.getToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(
                        "Dashboard Info Loaded Successfully",
                        user
                ));
    }
}
