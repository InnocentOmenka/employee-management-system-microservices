package com.innocodes.employee_management_service.service.impl;


import com.innocodes.employee_management_service.dto.response.ApiResponse;
import com.innocodes.employee_management_service.dto.request.DepartmentRequest;
import com.innocodes.employee_management_service.dto.response.DepartmentResponse;
import com.innocodes.employee_management_service.entity.Department;
import com.innocodes.employee_management_service.entity.User;
import com.innocodes.employee_management_service.enums.Role;
import com.innocodes.employee_management_service.exceptions.CustomException;
import com.innocodes.employee_management_service.repository.DepartmentRepository;
import com.innocodes.employee_management_service.repository.UserRepository;
import com.innocodes.employee_management_service.service.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RoleValidator roleValidator;

    /**
     * Create new department (Admin only)
     */
    public ApiResponse<DepartmentResponse> createDepartment(DepartmentRequest request, String token,  String creatorEmail) {
        roleValidator.checkIfAdmin(token);

        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .managerEmail(request.getManagerEmail())
                .createdAt(LocalDateTime.now())
                .build();

        departmentRepository.save(department);
        log.info("Department '{}' created by Admin {}", department.getName(), creatorEmail);

        return ApiResponse.success("Department created successfully", toResponse(department));
    }

    /**
     * Update department (Admin only)
     */
    public ApiResponse<DepartmentResponse> updateDepartment(Long id, DepartmentRequest request, String updaterEmail, String token) {
        roleValidator.checkIfAdmin(token);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Department not found with ID: " + id));

        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setManagerEmail(request.getManagerEmail());
        departmentRepository.save(department);

        log.info("Department '{}' updated by Admin {}", department.getName(), updaterEmail);
        return ApiResponse.success("Department updated successfully", toResponse(department));
    }

    /**
     * Delete department (Admin only)
     */
    public ApiResponse<Void> deleteDepartment(Long id, String deleterEmail, String token) {
        roleValidator.checkIfAdmin(token);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Cannot delete — department not found with ID: " + id));

        departmentRepository.delete(department);
        log.info("Department '{}' deleted by Admin {}", department.getName(), deleterEmail);

        return ApiResponse.success("Department deleted successfully", null);
    }

    /**
     * Get all departments (Admin only)
     */
    public ApiResponse<List<DepartmentResponse>> getAllDepartments(String requesterEmail, String token) {
        roleValidator.checkIfAdmin(token);

        List<DepartmentResponse> departments = departmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        log.info("All departments retrieved by {}", requesterEmail);
        return ApiResponse.success("All departments fetched successfully", departments);
    }

    /**
     * ✅ Get department by ID (Admin only)
     */
    public ApiResponse<DepartmentResponse> getDepartmentById(Long id, String requesterEmail, String token) {
        roleValidator.checkIfAdmin(token);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Department not found with ID: " + id));

        log.info("Department '{}' details fetched by {}", department.getName(), requesterEmail);
        return ApiResponse.success("Department details fetched successfully", toResponse(department));
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .managerEmail(department.getManagerEmail())
                .createdAt(department.getCreatedAt())
                .build();
    }

    public ApiResponse<DepartmentResponse> assignManagerToDepartment(Long departmentId, Long managerId, String performedBy) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException("Department not found"));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new CustomException("Manager not found"));

        if (!manager.getRole().equals(Role.MANAGER)) {
            throw new CustomException("Specified user is not a manager");
        }

        department.setManagerEmail(manager.getEmail());
        departmentRepository.save(department);

        DepartmentResponse response = new DepartmentResponse(department);

        return ApiResponse.success("Manager assigned to department successfully", response);
    }

}