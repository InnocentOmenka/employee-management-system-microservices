package com.innocodes.auth_service.utils;

import com.innocodes.auth_service.entity.User;
import com.innocodes.auth_service.enums.Role;
import com.innocodes.auth_service.exceptions.CustomException;
import com.innocodes.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleValidator {

    private final UserRepository userRepository;

    public User checkIfAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found or unauthorized"));

        if (user.getRole() != Role.ADMIN) {
            throw new CustomException("Access denied: Only Admins can perform this action");
        }
        return user;
    }

    public User checkIfManager(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found or unauthorized"));

        if (user.getRole() != Role.MANAGER) {
            throw new CustomException("Access denied: Only Managers can perform this action");
        }
        return user;
    }

    public User checkIfEmployee(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found or unauthorized"));
    }
}
