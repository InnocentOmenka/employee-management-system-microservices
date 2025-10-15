package com.innocodes.auth_service.service.impl;

import com.innocodes.auth_service.dto.request.LoginRequest;
import com.innocodes.auth_service.dto.request.UserRequest;
import com.innocodes.auth_service.dto.response.ApiResponse;
import com.innocodes.auth_service.dto.response.AuthResponse;
import com.innocodes.auth_service.dto.response.UserResponse;
import com.innocodes.auth_service.entity.User;
import com.innocodes.auth_service.exceptions.CustomException;
import com.innocodes.auth_service.repository.UserRepository;
import com.innocodes.auth_service.service.AuthService;
import com.innocodes.auth_service.security.JwtUtil;
import com.innocodes.auth_service.util.RoleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleValidator roleValidator;

    public ApiResponse<UserResponse> registerUser(UserRequest request, String email, String token) {
        roleValidator.checkIfAdmin(token);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already exists");
        }

        String defaultPassword = request.getPassword() != null ? request.getPassword() : "password123";

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .status(request.getStatus())
                .password(passwordEncoder.encode(defaultPassword))
                .role(request.getRole())
                .departmentId(request.getDepartmentId())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .departmentId(user.getDepartmentId())
                .createdAt(LocalDateTime.now())
                .message("User registered successfully")
                .build();

        return ApiResponse.success("User registration successful", response);
    }

    /**
     * Login endpoint for all users.
     */
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed for email: {}", request.getEmail());
                    return new CustomException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for user: {}", request.getEmail());
            throw new CustomException("Invalid email or password");
        }

        Map<String, Object> claims = Map.of(
                "email", user.getEmail(),
                "role", user.getRole().name()
        );

        String token = jwtUtil.generateToken(claims);

        user.setPassword(null);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);

        log.info("User '{}' logged in successfully", request.getEmail());

        return AuthResponse.builder()
                .message("Login successful")
                .data(data)
                .build();
    }
}