package com.innocodes.employee_management_service.dto;

import com.innocodes.employee_management_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String departmentName;
    private LocalDateTime createdAt;
    private Role role;
}
