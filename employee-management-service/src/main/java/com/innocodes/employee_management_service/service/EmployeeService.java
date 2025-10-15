package com.innocodes.employee_management_service.service;

import com.innocodes.employee_management_service.dto.response.ApiResponse;
import com.innocodes.employee_management_service.dto.request.EmployeeRequest;
import com.innocodes.employee_management_service.dto.response.UserResponse;

import java.util.List;


public interface EmployeeService {
    ApiResponse<UserResponse> updateUser(Long id, EmployeeRequest request, String updaterEmail, String token);

    // 2️⃣ Delete a user
    ApiResponse<Void> deleteUser(Long id, String deleterEmail, String token);

    // 3️⃣ Get all users (Admin only)
    ApiResponse<List<UserResponse>> getAllUsers(String requesterEmail, String token);

    // 4️⃣ Get user by ID (Admin only)
    ApiResponse<UserResponse> getUser(String email, String token, Long id);

    // 5️⃣ Get all users within a department (Manager only)
    ApiResponse<List<UserResponse>> getUsersByDepartment(Long departmentId, String managerEmail);

    // 6️⃣ Get logged-in user's profile
    ApiResponse<UserResponse> getMyProfile(String userEmail);


}
