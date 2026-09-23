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
import org.groomi.groomidevbackend.auth.dto.verify_account.register.VerifyNewAccountRequest;
import org.groomi.groomidevbackend.auth.email_service.EmailService;
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException;
import org.groomi.groomidevbackend.auth.exception_handlers.login.UnverifiedAccountException;
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

    public RegisterResponse register(RegisterRequest request, EmailService emailService) {
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
       String token =  jwtService.generateToken(savedUser, TokenType.VERIFY_ACCOUNT);
       assert token != null;
       emailService.sendRegisterNewAccountEmail(
               savedUser.getEmail(),
               savedUser.getFirstName(),
               "http://localhost:8080/auth/verify-new-account?token="
               +token);
        return new RegisterResponse(
                savedUser.getId(),
                request.getEmail(),
                token
        );
    }

    public LoginResponse login(LoginRequest request){
        UserProfile user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(request.getEmail(), request.getPassword())
                );
        boolean valid =  passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        boolean verified =  user.getEmailVerified().equals(true);
        if (!valid) {
            throw new InvalidCredentialsException(request.getEmail(), request.getPassword());
        }
        if(!verified){
            throw new UnverifiedAccountException(user.getEmailVerified());
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
                    "http://localhost:8080/auth/verify-reset-password-token?token=" + token
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        //TODO: remove token from this
        return new SendVerificationLinkResponse("Password reset email sent: " + token);
    }

    public LogoutResponse logout(LogoutRequest request){
        //TODO: invalidate token
        return new LogoutResponse("User Logged out");
    }

    public void verifyAccount(String token) {
        if (!jwtService.isTokenType(token, TokenType.VERIFY_ACCOUNT)) {
            throw new IllegalArgumentException("Invalid password reset token");
        }
        UUID userId = jwtService.extractUserId(token);
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );
        user.setEmailVerified(true);
        userRepository.save(user);
        new VerifyAccountResponse("Account verified successfully.", user.getEmailVerified());
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request){
        var token =  request.getToken();
        if(!jwtService.isTokenType(token, TokenType.PASSWORD_RESET)){
            return new ChangePasswordResponse("Unable to reset password. Wrong Token Type.");
        }
        UUID userId =  jwtService.extractUserId(token);
        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var newPassword =  request.getNewPassword();

        var encodedPassword = passwordEncoder.encode(newPassword);

        user.setPasswordHash(encodedPassword);

        userRepository.save(user);
        return new ChangePasswordResponse("Password changed successfully");

    }
}