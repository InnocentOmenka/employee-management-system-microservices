package com.innocodes.employee_management_service.controller;


import com.innocodes.employee_management_service.dto.ApiResponse;
import com.innocodes.employee_management_service.dto.EmployeeRequest;
import com.innocodes.employee_management_service.service.EmployeeService;
import com.innocodes.employee_management_service.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtUtil jwtUtil;

    // 2️⃣ Update employee (Admin only)
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id,
            @RequestBody EmployeeRequest request) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(employeeService.updateUser(id, request, email, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(
            @RequestHeader("Authorization") String authorization,
            @PathVariable Long id) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(employeeService.deleteUser(id, email, token));
    }

    // 4️⃣ View all employees (Admin only)
    @GetMapping("/all-employees")
    public ResponseEntity<ApiResponse> getAllEmployees(
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(employeeService.getAllUsers(email, token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEmployees(
            @RequestHeader("Authorization") String authorization, @PathVariable Long id) {

        String token = authorization.substring(7);
        String email = jwtUtil.extractEmail(token);
        return ResponseEntity.ok(employeeService.getUser(email, token, id));
    }

    // 5️⃣ View employees by department (Manager only)
    @GetMapping("/department/{id}")
    public ResponseEntity<ApiResponse> getEmployeesByDepartment(
            @RequestHeader("Authorization") String authorization, @PathVariable Long id){

        String email = jwtUtil.extractEmail(authorization.substring(7));
        return ResponseEntity.ok(employeeService.getUsersByDepartment(id, email));
    }

    // 6️⃣ View own profile (Employee)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyProfile(
            @RequestHeader("Authorization") String authorization) {

        String email = jwtUtil.extractEmail(authorization.substring(7));
        return ResponseEntity.ok(employeeService.getMyProfile(email));
    }
}
