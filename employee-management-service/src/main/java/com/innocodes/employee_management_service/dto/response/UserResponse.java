package com.innocodes.employee_management_service.dto.response;


import com.innocodes.employee_management_service.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Long departmentId;
    private String status;
    private LocalDateTime createdAt;
}

