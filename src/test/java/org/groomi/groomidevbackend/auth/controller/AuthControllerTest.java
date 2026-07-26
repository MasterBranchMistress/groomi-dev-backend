package org.groomi.groomidevbackend.auth.controller;

import org.groomi.groomidevbackend.auth.AuthController;
import org.groomi.groomidevbackend.auth.AuthService;
import org.groomi.groomidevbackend.auth.auth_providers.AuthProvider;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.groomi.groomidevbackend.auth.dto.register.RegisterRequest;
import org.groomi.groomidevbackend.auth.dto.register.RegisterResponse;
import org.groomi.groomidevbackend.auth.exception_handlers.login.InvalidCredentialsException;
import org.groomi.groomidevbackend.auth.fixtures.UserRegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AuthService authService;
    @Test
    void loginReturnsOkWhenCredentialsAreValid() throws Exception {
        LoginResponse response = new LoginResponse(
                UUID.randomUUID(),
                "jimmie@example.com",
                "fake-jwt-token"
        );
        Mockito.when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);
        performLogin(response.email(), "Password123!")
                .andExpect(status().isOk());
    }
    @Test
    void loginReturns401WhenCredentialsAreInvalid() throws Exception {
        Mockito.when(authService.login(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException(
                        "wrong_login@example.com",
                        "WrongPassword123!"
                ));
        performLogin("wrong_login@example.com", "WrongPassword123!")
                .andExpect(status().isUnauthorized());
    }
    @Test
    void registerReturns201WhenRegistrationSucceeds() throws Exception{
        RegisterRequest request = UserRegisterRequest.isValidRegisterRequest();
        RegisterResponse response =  new RegisterResponse(UUID.randomUUID(), request.getEmail());
        Mockito.when(authService.register(any(RegisterRequest.class))).thenReturn(response);
        performRegister(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber(), request.getPassword()).andExpect(status().isCreated());
    }
    @Test
    void registerReturnsBadRequestWhenRequestIsInvalid() throws Exception {
        performRegister("", "Smith", "not-an-email", "777-777-7777", "").andExpect(status().isBadRequest());
        verify(authService, never()).register(any(RegisterRequest.class));
    }
    private ResultActions performLogin(
            String email,
            String password
    ) throws Exception {
        return mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "%s",
                        "password": "%s"
                    }
                    """.formatted(email, password)));
    }
    private ResultActions performRegister(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String password
    ) throws Exception{
        return mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                    {
                        "firstName": "%s",
                        "lastName": "%s",
                        "email": "%s",
                        "phoneNumber":"%s",
                        "password": "%s",
                        "authProvider":"%s"
                    }
                    """.formatted(firstName, lastName,email,phoneNumber, password, AuthProvider.LOCAL)));
    }
}

