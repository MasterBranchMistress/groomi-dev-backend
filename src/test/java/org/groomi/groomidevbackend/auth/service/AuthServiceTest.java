package org.groomi.groomidevbackend.auth.service;

import org.groomi.groomidevbackend.auth.AuthService;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;
import org.groomi.groomidevbackend.auth.dto.register.RegisterResponse;
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException;
import org.groomi.groomidevbackend.auth.exception_handlers.register.AccountAlreadyExistsException;
import org.groomi.groomidevbackend.auth.fixtures.UserLoginRequest;
import org.groomi.groomidevbackend.auth.fixtures.TestUser;
import org.groomi.groomidevbackend.auth.fixtures.UserRegisterRequest;
import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.user.UserProfile;
import org.groomi.groomidevbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    @Test
    void whenLoginRequestIsValid(){
        LoginRequest request =  UserLoginRequest.isValidLoginRequest();
        UserProfile user =  TestUser.isValidUser();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");
        LoginResponse response = authService.login(request);
        assertEquals(user.getId(), response.userId());
        assertEquals(user.getEmail(), response.email());
        assertEquals("fake-jwt-token", response.token());

    }
    @Test
    void whenLoginRequestHasInvalidEmail(){
        LoginRequest request = UserLoginRequest.hasInvalidEmail();
        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );
    }
    @Test
    void whenLoginRequestHasInvalidPassword(){

        UserProfile user = TestUser.isValidUser();
        LoginRequest request = UserLoginRequest.hasInvalidPassword();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );
    }

    @Test
    void whenRegisterRequestIsValid(){
        RegisterRequest request = UserRegisterRequest.isValidRegisterRequest();
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("hashed-password");
        when(userRepository.save(any(UserProfile.class)))
                .thenReturn(TestUser.isValidUser());
        RegisterResponse response = authService.register(request);
        assertEquals(request.getEmail(), response.getEmail());
        verify(passwordEncoder)
                .encode(request.getPassword());
        verify(userRepository)
                .save(any(UserProfile.class));
    }
    @Test
    void whenAccountAlreadyExistsWhenRegistering() {
        RegisterRequest request = UserRegisterRequest.isExistingUserAlready();
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);
        assertThrows(
                AccountAlreadyExistsException.class,
                () -> authService.register(request)
        );
        verify(userRepository, never())
                .save(any(UserProfile.class));
    }
}
