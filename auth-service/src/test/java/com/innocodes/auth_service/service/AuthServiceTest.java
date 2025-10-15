package com.innocodes.auth_service.service;


import com.innocodes.auth_service.dto.request.LoginRequest;
import com.innocodes.auth_service.dto.request.UserRequest;
import com.innocodes.auth_service.dto.response.ApiResponse;
import com.innocodes.auth_service.dto.response.AuthResponse;
import com.innocodes.auth_service.dto.response.UserResponse;
import com.innocodes.auth_service.entity.User;
import com.innocodes.auth_service.enums.Role;
import com.innocodes.auth_service.exceptions.CustomException;
import com.innocodes.auth_service.repository.UserRepository;
import com.innocodes.auth_service.security.JwtUtil;
import com.innocodes.auth_service.util.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RoleValidator roleValidator;

    @InjectMocks private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldCreateUser_WhenValidRequest() {
        // given
        UserRequest request = new UserRequest("John", "Doe", "john@example.com", "password123", Role.EMPLOYEE, 1L, "ACTIVE", LocalDateTime.now());
        String token = "jwt-token";
        String email = "admin@example.com";

        when(roleValidator.checkIfAdmin(token)).thenReturn(new User());
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        ApiResponse<UserResponse> response = authService.registerUser(request, token, email);

        // then
        assertNotNull(response);
        assertEquals("User registration successful", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailExists() {
        UserRequest request = new UserRequest("John", "Doe", "joan@gmail.com", "pass", Role.EMPLOYEE, 1L, "ACTIVE", LocalDateTime.now());
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(CustomException.class, () -> authService.registerUser(request, "token", "admin@example.com"));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsValid() {
        LoginRequest request = new LoginRequest("john@example.com", "password123");
        User user = new User(1L, "John", "Doe", "john@example.com", "encoded", Role.MANAGER, 1L, "ACTIVE", LocalDateTime.now());


        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyMap())).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals("Login successful", response.getMessage());
        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertTrue(data.containsKey("token"));
        verify(jwtUtil).generateToken(anyMap());
    }

    @Test
    void login_ShouldThrowException_WhenInvalidPassword() {
        LoginRequest request = new LoginRequest("joan@gmail.com", "wrongpass");
        User user = new User(1L, "joan", "ijuo", "joan@gmail.com", "encoded", Role.MANAGER, 2L, "ACTIVE", LocalDateTime.now());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .thenReturn(false); // 👈 simulate mismatch

        assertThrows(CustomException.class, () -> authService.login(request));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("joan@gmail.com", "joan@123");
        User user = new User(1L, "joan", "ijuo", "joan@gmail.com", "encoded", Role.MANAGER, 2L, "ACTIVE", LocalDateTime.now());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .thenReturn(true); // 👈 simulate correct password
        when(jwtUtil.generateToken(anyMap())).thenReturn("mocked-token");

        AuthResponse response = authService.login(request);

        assertEquals("Login successful", response.getMessage());
        Map<String, Object> data = (Map<String, Object>) response.getData();
        assertEquals("mocked-token", data.get("token"));
        assertEquals("joan@gmail.com", ((User) data.get("user")).getEmail());
    }


}
