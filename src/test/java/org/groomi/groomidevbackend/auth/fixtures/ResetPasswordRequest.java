package org.groomi.groomidevbackend.auth.fixtures;

import org.groomi.groomidevbackend.auth.dto.change_password.ChangePasswordRequest;
import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;
import org.groomi.groomidevbackend.dashboard.UserDashboard;

public class ResetPasswordRequest {
    public static ChangePasswordRequest isValidResetPasswordRequest(JwtService jwtService){
        ChangePasswordRequest request =  new ChangePasswordRequest();
        UserDashboard user =  TestUser.hasAllDashboardInformation();
        String token =  jwtService.generateToken(user, TokenType.PASSWORD_RESET);
        request.setToken(token);
        request.setNewPassword("testPass123");
        return request;
    }
    public static ChangePasswordRequest invalidTokenType(JwtService jwtService){
        ChangePasswordRequest request =  new ChangePasswordRequest();
        request.setToken("invalid token");
        request.setNewPassword("testPass123");
        return request;
    }
}
