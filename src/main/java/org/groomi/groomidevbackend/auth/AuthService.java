package org.groomi.groomidevbackend.auth;

import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;
import org.groomi.groomidevbackend.auth.dto.change_password.ChangePasswordRequest;
import org.groomi.groomidevbackend.auth.dto.change_password.ChangePasswordResponse;
import org.groomi.groomidevbackend.auth.dto.forgot_password.SendVerificationLinkToUsersEmail;
import org.groomi.groomidevbackend.auth.dto.forgot_password.SendVerificationLinkResponse;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.groomi.groomidevbackend.auth.dto.logout.LogoutRequest;
import org.groomi.groomidevbackend.auth.dto.logout.LogoutResponse;
import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;
import org.groomi.groomidevbackend.auth.dto.register.RegisterResponse;
import org.groomi.groomidevbackend.auth.dto.verify_account.forgot_password.VerifyAccountResponse;
import org.groomi.groomidevbackend.auth.email_service.EmailService;
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException;
import org.groomi.groomidevbackend.auth.exception_handlers.register.AccountAlreadyExistsException;
import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;
import org.groomi.groomidevbackend.user.UserProfile;
import org.groomi.groomidevbackend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AccountAlreadyExistsException(request.getEmail());
        }
        UserProfile user = new UserProfile(
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getEmail(),
                false,
                passwordEncoder.encode(request.getPassword()),
                AuthProvider.LOCAL
        );

        UserProfile savedUser = userRepository.save(user);
        return new RegisterResponse(
                savedUser.getId(),
                request.getEmail()
        );
    }

    public LoginResponse login(LoginRequest request){
        UserProfile user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(request.getEmail(), request.getPassword())
                );
        boolean valid =  passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!valid) {
            throw new InvalidCredentialsException(request.getEmail(), request.getPassword());
        }
        String token =  jwtService.generateToken(user, TokenType.SESSION_LOGGED_IN);
        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                token

        );
    }

    public SendVerificationLinkResponse sendVerificationLinkToUsersEmail(SendVerificationLinkToUsersEmail request, EmailService emailService){
        UserProfile user =  userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user, TokenType.PASSWORD_RESET);
        try{

            assert token != null;
            emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    "groomr://reset-password?token="+token
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return new SendVerificationLinkResponse("Password reset email sent: " + token);
    }

    public LogoutResponse logout(LogoutRequest request){
        //TODO: invalidate token
        return new LogoutResponse("User Logged out");
    }

    public VerifyAccountResponse verifyAccount(String token) {
        if (!jwtService.isTokenType(token, TokenType.PASSWORD_RESET)) {
            throw new IllegalArgumentException("Invalid password reset token");
        }
        UUID userId = jwtService.extractUserId(token);
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );
        if(!user.getEmailVerified()){
            return new VerifyAccountResponse("Account has not been verified.", false);
        }
        userRepository.save(user);
        return new VerifyAccountResponse("Account verified successfully. Reset Password permitted.", true);
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request){
        var token =  request.getToken();
        if(!jwtService.isTokenType(token, TokenType.PASSWORD_RESET)){
            return new ChangePasswordResponse("Unable to reset password. Wrong Token Type.");
        }
        UUID userId =  jwtService.extractUserId(token);
        UserProfile user =  userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        var newPassword =  request.getNewPassword();
        var encodedPassword = passwordEncoder.encode(newPassword);
        user.setPasswordHash(encodedPassword);
        userRepository.save(user);
        return new ChangePasswordResponse("Password changed successfully");

    }
}