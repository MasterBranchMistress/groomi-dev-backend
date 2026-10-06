package org.groomi.groomidevbackend.auth.service;

import org.groomi.groomidevbackend.auth.AuthService;
import org.groomi.groomidevbackend.auth.dto.change_password.ChangePasswordRequest;
import org.groomi.groomidevbackend.auth.dto.change_password.ChangePasswordResponse;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;
import org.groomi.groomidevbackend.auth.dto.register.RegisterResponse;
import org.groomi.groomidevbackend.auth.email_service.EmailService;
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException;
import org.groomi.groomidevbackend.auth.exception_handlers.register.AccountAlreadyExistsException;
import org.groomi.groomidevbackend.auth.fixtures.ResetPasswordRequest;
import org.groomi.groomidevbackend.auth.fixtures.UserLoginRequest;
import org.groomi.groomidevbackend.auth.fixtures.TestUser;
import org.groomi.groomidevbackend.auth.fixtures.UserRegisterRequest;
import org.groomi.groomidevbackend.auth.token_generator.JwtService;
import org.groomi.groomidevbackend.auth.token_generator.token_types.TokenType;
import org.groomi.groomidevbackend.dashboard.UserDashboard;
import org.groomi.groomidevbackend.dashboard.UserDashboardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserDashboardRepository userDashboardRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;
    @Mock
    private EmailService emailService;

    @Test
    void whenLoginRequestIsValid(){
        LoginRequest request =  UserLoginRequest.isValidLoginRequest();
        UserDashboard user =  TestUser.hasAllDashboardInformation();
        when(userDashboardRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(user, TokenType.SESSION_LOGGED_IN)).thenReturn("fake-jwt-token");
        LoginResponse response = authService.login(request);
        assertEquals(user.getId(), response.userId());
        assertEquals(user.getEmail(), response.email());
        assertEquals("fake-jwt-token", response.token());

    }
    @Test
    void whenLoginRequestHasInvalidEmail(){
        LoginRequest request = UserLoginRequest.hasInvalidEmail();
        when(userDashboardRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );
    }
    @Test
    void whenLoginRequestHasInvalidPassword(){

        UserDashboard user = TestUser.hasAllDashboardInformation();
        LoginRequest request = UserLoginRequest.hasInvalidPassword();

        when(userDashboardRepository.findByEmail(request.getEmail()))
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
        when(userDashboardRepository.save(any(UserDashboard.class)))
                .thenReturn(TestUser.hasAllDashboardInformation());
        when(jwtService.generateToken(any(UserDashboard.class), any(TokenType.class)))
                .thenReturn("test-token");
        RegisterResponse response = authService.register(request, emailService);
        System.out.println("Request email: " + request.getEmail());
        System.out.println("Response email: " + response.email());
        assertEquals(request.getEmail(), response.email());
        verify(passwordEncoder)
                .encode(request.getPassword());
        verify(userDashboardRepository)
                .save(any(UserDashboard.class));
    }
    @Test
    void whenAccountAlreadyExistsWhenRegistering() {
        RegisterRequest request = UserRegisterRequest.isExistingUserAlready();
        when(userDashboardRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);
        assertThrows(
                AccountAlreadyExistsException.class,
                () -> authService.register(request, emailService)
        );
        verify(userDashboardRepository, never())
                .save(any(UserDashboard.class));
    }
    @Test
    void whenResetPasswordIsSuccessful(){
        ChangePasswordRequest request =
                ResetPasswordRequest.isValidResetPasswordRequest(jwtService);
        UserDashboard user = TestUser.hasAllDashboardInformation();

        when(jwtService.isTokenType(
                request.getToken(), TokenType.PASSWORD_RESET
        )).thenReturn(true);
        when(jwtService.extractUserId(request.getToken()))
                .thenReturn(user.getId());
        when(userDashboardRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode(request.getNewPassword()))
                .thenReturn("hashed-password");
        ChangePasswordResponse response =
                authService.changePassword(request);
        ArgumentCaptor<UserDashboard> userCaptor =
                ArgumentCaptor.forClass(UserDashboard.class);
        verify(userDashboardRepository).save(userCaptor.capture());
        UserDashboard savedUser = userCaptor.getValue();
        assertEquals("hashed-password", savedUser.getPasswordHash());
        assertEquals("Password changed successfully", response.message());
        verify(passwordEncoder).encode(request.getNewPassword());
    }

    @Test
    void whenPasswordResetTokenIsWrongType(){
        ChangePasswordRequest request =  ResetPasswordRequest.invalidTokenType(jwtService);
        ChangePasswordResponse response =  authService.changePassword(request);
        assertEquals("Unable to reset password. Wrong Token Type.", response.message());
    }
}
