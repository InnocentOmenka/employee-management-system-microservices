package com.innocodes.employee_management_service.service;


import com.innocodes.employee_management_service.dto.response.ApiResponse;
import com.innocodes.employee_management_service.dto.request.DepartmentRequest;
import com.innocodes.employee_management_service.dto.response.DepartmentResponse;

public interface DepartmentService {
    // 1️⃣ Create new department
    ApiResponse createDepartment(DepartmentRequest request, String token, String email);

    // 2️⃣ Update department
    ApiResponse updateDepartment(Long id, DepartmentRequest request, String email, String token);

    // 3️⃣ Delete department
    ApiResponse deleteDepartment(Long id, String email, String token);

    // 4️⃣ Get single department by ID
    ApiResponse getDepartmentById(Long id, String email, String token);

    // 5️⃣ Get all departments
    ApiResponse getAllDepartments(String email, String token);

    // 6️⃣ Assign manager to department
    ApiResponse<DepartmentResponse> assignManagerToDepartment(Long departmentId, Long managerId, String email);


}