package org.groomi.groomidevbackend.auth;

import jakarta.validation.Valid;
import org.groomi.groomidevbackend.auth.dto.change_password.ChangePasswordRequest;
import org.groomi.groomidevbackend.auth.dto.change_password.ChangePasswordResponse;
import org.groomi.groomidevbackend.auth.dto.forgot_password.*;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.groomi.groomidevbackend.auth.dto.logout.LogoutRequest;
import org.groomi.groomidevbackend.auth.dto.logout.LogoutResponse;
import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;
import org.groomi.groomidevbackend.auth.dto.register.RegisterResponse;
import org.groomi.groomidevbackend.auth.dto.verify_account.forgot_password.VerifyAccountResponse;
import org.groomi.groomidevbackend.auth.email_service.EmailService;
import org.groomi.groomidevbackend.shared_packages.api_response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService; this.emailService=  emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
           @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                        new ApiResponse<>("User registered successfully",
                                authService.register(request)
                        )
                );
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Login Successful", authService.login(request)))
    ;}

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(
            @Valid @RequestBody LogoutRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Logout Successful", authService.logout(request)))
                ;}

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<SendVerificationLinkResponse>> sendVerificationLinkToUsersEmail(
            @Valid @RequestBody SendVerificationLinkToUsersEmail request
            ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Email successfully sent.", authService.sendVerificationLinkToUsersEmail(request, emailService)));
    }
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<ChangePasswordResponse>> resetUserPassword(
            @Valid @RequestBody ChangePasswordRequest request
            ){
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("password changed successfully", authService.changePassword(request)));
    }
    @GetMapping("/verify-password-reset-link")
    public ResponseEntity<ApiResponse<VerifyAccountResponse>> verifyEmail(
            @RequestParam String token
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Account verified successfully",
                        authService.verifyAccount(token)
                )
        );
    }
}
