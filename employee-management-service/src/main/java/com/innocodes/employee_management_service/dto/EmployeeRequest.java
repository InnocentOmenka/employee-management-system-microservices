package com.innocodes.employee_management_service.dto;

import com.innocodes.employee_management_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private Long departmentId;
    private Role role;
}
