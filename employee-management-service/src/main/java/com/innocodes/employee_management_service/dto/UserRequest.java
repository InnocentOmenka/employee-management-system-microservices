package com.innocodes.employee_management_service.dto;

import com.innocodes.employee_management_service.enums.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;        // Optional, default can be assigned
    private Role role;
    private Long departmentId;      // If linked to a department
}
