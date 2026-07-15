package org.groomi.groomidevbackend.auth;

import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;
import org.groomi.groomidevbackend.auth.dto.register.RegisterResponse;
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException;
import org.groomi.groomidevbackend.auth.exception_handlers.register.AccountAlreadyExistsException;
import org.groomi.groomidevbackend.user.UserProfile;
import org.groomi.groomidevbackend.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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


        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName() + " logged in!"
        );
    }
}