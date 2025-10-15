package com.innocodes.employee_management_service.service;

import com.innocodes.employee_management_service.dto.ApiResponse;
import com.innocodes.employee_management_service.dto.EmployeeRequest;
import com.innocodes.employee_management_service.dto.UserResponse;
import com.innocodes.employee_management_service.entity.Department;
import com.innocodes.employee_management_service.entity.User;
import com.innocodes.employee_management_service.enums.Role;
import com.innocodes.employee_management_service.exceptions.CustomException;
import com.innocodes.employee_management_service.repository.DepartmentRepository;
import com.innocodes.employee_management_service.repository.UserRepository;
import com.innocodes.employee_management_service.utils.RoleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private RoleValidator roleValidator;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequest employeeRequest;
    private User user;
    private Department department;

    @BeforeEach
    void setUp() {
        employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("John");
        employeeRequest.setLastName("Doe");
        employeeRequest.setEmail("john@company.com");
        employeeRequest.setRole(Role.EMPLOYEE);
        employeeRequest.setDepartmentId(1L);
        employeeRequest.setStatus("ACTIVE");

        department = new Department();
        department.setId(1L);
        department.setName("Finance");

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@company.com")
                .role(Role.EMPLOYEE)
                .status("ACTIVE")
                .departmentId(1L)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ✅ 1. Update User - Success
    @Test
    void updateUser_ShouldReturnSuccess_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(userRepository.save(any(User.class))).thenReturn(user);

        ApiResponse<UserResponse> response =
                employeeService.updateUser(1L, employeeRequest, "admin@company.com", "mockToken");

        assertEquals("User updated successfully", response.getMessage());
        assertEquals("john@company.com", response.getData().getEmail());
        verify(roleValidator).checkIfAdmin(anyString());
    }

    // ❌ 2. Update User - Not Found
    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CustomException.class,
                () -> employeeService.updateUser(99L, employeeRequest, "admin@company.com", "mockToken"));
    }

    // ✅ 3. Delete User - Success
    @Test
    void deleteUser_ShouldReturnSuccess() {
        doNothing().when(userRepository).deleteById(1L);

        ApiResponse<Void> response =
                employeeService.deleteUser(1L, "admin@company.com", "mockToken");

        assertEquals("User deleted successfully", response.getMessage());
        verify(userRepository).deleteById(1L);
        verify(roleValidator).checkIfAdmin(anyString());
    }

    // ✅ 4. Get All Users
    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        ApiResponse<List<UserResponse>> response =
                employeeService.getAllUsers("admin@company.com", "mockToken");

        assertEquals(1, response.getData().size());
        assertEquals("All users fetched successfully", response.getMessage());
        verify(roleValidator).checkIfAdmin(anyString());
    }

    // ✅ 5. Get My Profile - Success
    @Test
    void getMyProfile_ShouldReturnUser_WhenExists() {
        when(userRepository.findByEmail("john@company.com"))
                .thenReturn(Optional.of(user));

        ApiResponse<UserResponse> response =
                employeeService.getMyProfile("john@company.com");

        assertEquals("Profile fetched successfully", response.getMessage());
        assertEquals("john@company.com", response.getData().getEmail());
    }

    // ❌ 6. Get My Profile - Not Found
    @Test
    void getMyProfile_ShouldThrowException_WhenNotFound() {
        when(userRepository.findByEmail("missing@company.com"))
                .thenReturn(Optional.empty());

        assertThrows(CustomException.class,
                () -> employeeService.getMyProfile("missing@company.com"));
    }
}
