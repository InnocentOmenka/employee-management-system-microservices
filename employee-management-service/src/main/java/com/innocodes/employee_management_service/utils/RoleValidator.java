package com.innocodes.employee_management_service.utils;

import com.innocodes.employee_management_service.entity.User;
import com.innocodes.employee_management_service.enums.Role;
import com.innocodes.employee_management_service.exceptions.CustomException;
import com.innocodes.employee_management_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleValidator {

    private final JwtUtil jwtUtil;

    public void checkIfAdmin(String token) {
        String role = jwtUtil.extractRole(token);
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied: Only Admins can perform this action");
        }
    }

    public void checkIfManager(String token) {
        String role = jwtUtil.extractRole(token);
        if (!"MANAGER".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied: Only Managers can perform this action");
        }
    }

    public void checkIfEmployee(String token) {
        String role = jwtUtil.extractRole(token);
        if (!"EMPLOYEE".equalsIgnoreCase(role)) {
            throw new CustomException("Access denied: Only Employees can perform this action");
        }
    }
}
