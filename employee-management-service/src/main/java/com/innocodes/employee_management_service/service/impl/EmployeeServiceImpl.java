package com.innocodes.employee_management_service.service.impl;

import com.innocodes.employee_management_service.dto.response.ApiResponse;
import com.innocodes.employee_management_service.dto.request.EmployeeRequest;
import com.innocodes.employee_management_service.dto.response.UserResponse;
import com.innocodes.employee_management_service.entity.User;
import com.innocodes.employee_management_service.enums.Role;
import com.innocodes.employee_management_service.exceptions.CustomException;
import com.innocodes.employee_management_service.repository.DepartmentRepository;
import com.innocodes.employee_management_service.repository.UserRepository;
import com.innocodes.employee_management_service.service.EmployeeService;
import com.innocodes.employee_management_service.utils.RoleValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleValidator roleValidator;

    public ApiResponse<UserResponse> updateUser(Long id, EmployeeRequest request, String updaterEmail, String token) {
        roleValidator.checkIfAdmin(token);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found"));

        if (request.getDepartmentId() != null) {
            departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new CustomException("Invalid department ID"));
            user.setDepartmentId(request.getDepartmentId());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setStatus(request.getStatus());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        log.info("User {} updated by Admin {}", user.getEmail(), updaterEmail);
        return ApiResponse.success("User updated successfully", toResponse(user));
    }

    public ApiResponse<Void> deleteUser(Long id, String deleterEmail, String token) {
        roleValidator.checkIfAdmin(token);

        userRepository.deleteById(id);
        log.info("User with ID {} deleted by Admin {}", id, deleterEmail);
        return ApiResponse.success("User deleted successfully", null);
    }

    public ApiResponse<List<UserResponse>> getAllUsers(String requesterEmail, String token) {
        roleValidator.checkIfAdmin(token);

        List<UserResponse> users = userRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());

        return ApiResponse.success("All users fetched successfully", users);
    }

    public ApiResponse<UserResponse> getUser(String email, String token, Long id) {
        roleValidator.checkIfAdmin(token);
        userRepository.findById(id);
        log.info("User with ID {} fetched by Admin {}", id, email);
        return ApiResponse.success("User fetched successfully", toResponse(userRepository.findById(id).get()));
    }

    public ApiResponse<List<UserResponse>> getUsersByDepartment(Long departmentId, String managerEmail) {
        User manager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new CustomException("Unauthorized access"));

        if (manager.getRole() != Role.MANAGER) {
            throw new CustomException("Only Managers can view department employees");
        }

        List<UserResponse> users = userRepository.findByDepartmentId(departmentId)
                .stream().map(this::toResponse).collect(Collectors.toList());

        return ApiResponse.success("Users in department fetched successfully", users);
    }

    public ApiResponse<UserResponse> getMyProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException("User not found"));

        return ApiResponse.success("Profile fetched successfully", toResponse(user));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .departmentId(user.getDepartmentId())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
