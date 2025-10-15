package com.innocodes.auth_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocodes.auth_service.dto.request.LoginRequest;
import com.innocodes.auth_service.dto.request.UserRequest;
import com.innocodes.auth_service.dto.response.ApiResponse;
import com.innocodes.auth_service.dto.response.AuthResponse;
import com.innocodes.auth_service.dto.response.UserResponse;
import com.innocodes.auth_service.enums.Role;
import com.innocodes.auth_service.service.AuthService;
import com.innocodes.auth_service.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;


@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = com.innocodes.auth_service.AuthServiceApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ShouldReturnSuccessResponse_WhenCredentialsAreValid() throws Exception {
        LoginRequest request = new LoginRequest("admin@company.com", "password@123");

        AuthResponse mockResponse = AuthResponse.builder()
                .message("Login successful")
                .data(Map.of("token", "mocked-jwt"))
                .build();

        Mockito.when(authService.login(ArgumentMatchers.any(LoginRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value("mocked-jwt"));
    }

    @Test
    void registerUser_ShouldReturnSuccessResponse_WhenAdminRegistersUser() throws Exception {
        String token = "Bearer mocked-jwt";
        UserRequest request = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@company.com")
                .password("securepass")
                .role(Role.EMPLOYEE)
                .departmentId(1L)
                .status("ACTIVE")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .email("john@company.com")
                .firstName("John")
                .lastName("Doe")
                .message("User registered successfully")
                .build();

        ApiResponse<UserResponse> mockApiResponse = ApiResponse.success("User registration successful", userResponse);

        Mockito.when(jwtUtil.extractEmail(ArgumentMatchers.anyString())).thenReturn("admin@company.com");
        Mockito.when(authService.registerUser(ArgumentMatchers.any(UserRequest.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenReturn(mockApiResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registration successful"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("john@company.com"));
    }
}
