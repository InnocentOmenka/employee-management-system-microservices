package com.innocodes.auth_service.dto.request;

import com.innocodes.auth_service.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private Long departmentId;
    private String status;
    private LocalDateTime createdAt;
}

