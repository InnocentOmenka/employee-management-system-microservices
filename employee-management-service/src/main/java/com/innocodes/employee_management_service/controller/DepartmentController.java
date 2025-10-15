package com.innocodes.employee_management_service.controller;

import com.innocodes.employee_management_service.dto.response.ApiResponse;
import com.innocodes.employee_management_service.dto.request.DepartmentRequest;
import com.innocodes.employee_management_service.dto.response.DepartmentResponse;
import com.innocodes.employee_management_service.service.DepartmentService;
import com.innocodes.employee_management_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final JwtUtil jwtUtil;

    // 1️⃣ Create new department
    @PostMapping
    public ResponseEntity<ApiResponse> createDepartment(
            @RequestHeader("Authorization") String authorization,
            @RequestBody DepartmentRequest request) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(departmentService.createDepartment(request, token, email));
    }

    // 2️⃣ Update department
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDepartment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody DepartmentRequest request) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(departmentService.updateDepartment(id, request, email, token));
    }

    // 3️⃣ Delete department
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDepartment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(departmentService.deleteDepartment(id, email, token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getDepartment(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(departmentService.getDepartmentById(id, email, token));
    }

    // 4️⃣ View all departments
    @GetMapping
    public ResponseEntity<ApiResponse> getAllDepartments(
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(departmentService.getAllDepartments(email, token));
    }

    @PostMapping("/{departmentId}/assign-manager/{managerId}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> assignManagerToDepartment(
            @PathVariable Long departmentId,
            @PathVariable Long managerId,
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.split(" ")[1];
        String email = jwtUtil.extractEmail(token);

        return ResponseEntity.ok(departmentService.assignManagerToDepartment(departmentId, managerId, email));
    }

}
