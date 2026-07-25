package org.groomi.groomidevbackend.auth.controller;

import org.groomi.groomidevbackend.auth.AuthController;
import org.groomi.groomidevbackend.auth.AuthService;
import org.groomi.groomidevbackend.auth.dto.login.LoginRequest;
import org.groomi.groomidevbackend.auth.dto.login.LoginResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

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
        Mockito.when(authService.login(Mockito.any(LoginRequest.class)))
                .thenReturn(response);
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "jimmie@example.com",
                                    "password": "Password123!"
                                }
                                """))
                .andExpect(status().isOk());
    }
}