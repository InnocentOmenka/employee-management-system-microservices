package com.innocodes.employee_management_service.utils;

import com.innocodes.employee_management_service.dto.ApiResponse;
import com.innocodes.employee_management_service.dto.UserRequest;
import com.innocodes.employee_management_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "http://localhost:8081/api/v1/auth")
public interface AuthClient {

    @PostMapping("/register")
    ApiResponse<UserResponse> registerUser(@RequestBody UserRequest request);
}

