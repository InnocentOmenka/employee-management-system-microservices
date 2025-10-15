package com.innocodes.employee_management_service.integration;

import com.innocodes.employee_management_service.dto.ApiResponse;
import com.innocodes.employee_management_service.dto.EmployeeRequest;
import com.innocodes.employee_management_service.dto.UserResponse;
import com.innocodes.employee_management_service.entity.Department;
import com.innocodes.employee_management_service.entity.User;
import com.innocodes.employee_management_service.enums.Role;
import com.innocodes.employee_management_service.repository.DepartmentRepository;
import com.innocodes.employee_management_service.repository.UserRepository;
import com.innocodes.employee_management_service.service.EmployeeService;
import com.innocodes.employee_management_service.util.JwtTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        properties = {
                "spring.config.location=classpath:application-integration.yml"
        }
)
@ActiveProfiles("integration")
@Transactional
class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;
    private User user;
    private EmployeeRequest employeeRequest;
    private String adminToken;

    @BeforeEach
    void setup() {
        // Create a department
        department = new Department();
        department.setName("Finance");
        department = departmentRepository.save(department);

        // Create an existing user
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@company.com")
                .password("password123")
                .role(Role.EMPLOYEE)
                .status("ACTIVE")
                .departmentId(department.getId())
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // Prepare the request
        employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Smith");
        employeeRequest.setEmail("john@company.com");
        employeeRequest.setRole(Role.EMPLOYEE);
        employeeRequest.setDepartmentId(department.getId());
        employeeRequest.setStatus("ACTIVE");

        // Generate mock JWT token for admin
        adminToken = JwtTestUtil.generateToken("admin@company.com", "ADMIN");
    }

    @Test
    void updateUser_ShouldUpdateSuccessfully() {
        ApiResponse<UserResponse> response =
                employeeService.updateUser(user.getId(), employeeRequest, "admin@company.com", adminToken);

        assertEquals("User updated successfully", response.getMessage());
        assertEquals("Smith", response.getData().getLastName());

        User updated = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("Smith", updated.getLastName());
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        assertThrows(RuntimeException.class, () ->
                employeeService.updateUser(999L, employeeRequest, "admin@company.com", adminToken));
    }

    @Test
    void getAllUsers_ShouldReturnUsers() {
        ApiResponse<List<UserResponse>> response =
                employeeService.getAllUsers("admin@company.com", adminToken);

        assertFalse(response.getData().isEmpty());
        assertEquals("All users fetched successfully", response.getMessage());
    }

    @Test
    void getMyProfile_ShouldReturnUser() {
        ApiResponse<UserResponse> response =
                employeeService.getMyProfile("john@company.com");

        assertEquals("Profile fetched successfully", response.getMessage());
        assertEquals("john@company.com", response.getData().getEmail());
    }
}
