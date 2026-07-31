package org.groomi.groomidevbackend.auth;

import lombok.val;
import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;
import org.groomi.groomidevbackend.auth.dto.forgot_password.ForgotPasswordRequest;
import org.groomi.groomidevbackend.auth.dto.forgot_password.ForgotPasswordResponse;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.groomi.groomidevbackend.auth.dto.logout.LogoutRequest;
import org.groomi.groomidevbackend.auth.dto.logout.LogoutResponse;
import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;
import org.groomi.groomidevbackend.auth.dto.register.RegisterResponse;
import org.groomi.groomidevbackend.auth.email_service.EmailService;
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException;
import org.groomi.groomidevbackend.auth.exception_handlers.register.AccountAlreadyExistsException;
import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.user.UserProfile;
import org.groomi.groomidevbackend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        String token =  jwtService.generateToken(user);
        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                token

        );
    }

    public ForgotPasswordResponse submitEmail(ForgotPasswordRequest request, EmailService emailService){
        UserProfile user =  userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        try{
            emailService.sendEmail(
                    user.getEmail(),
                    "Reset Your Password",
                    "Email was successful - REMOVE THIS TOKEN OUT OF DEV " + token
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return new ForgotPasswordResponse("Password reset email sent");
    }

    public LogoutResponse logout(LogoutRequest request){
        //TODO: invalidate token
        return new LogoutResponse("User Logged out");
    }
}