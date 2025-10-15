package com.innocodes.auth_service.controller;

import com.innocodes.auth_service.dto.request.LoginRequest;
import com.innocodes.auth_service.dto.request.UserRequest;
import com.innocodes.auth_service.dto.response.ApiResponse;
import com.innocodes.auth_service.dto.response.AuthResponse;
import com.innocodes.auth_service.dto.response.UserResponse;
import com.innocodes.auth_service.service.AuthService;
import com.innocodes.auth_service.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestHeader ("Authorization") String authorization,
                                                                  @RequestBody UserRequest request) {
        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(authService.registerUser(request, token, email));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
