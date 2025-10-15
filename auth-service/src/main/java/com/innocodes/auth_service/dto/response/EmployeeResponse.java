package com.innocodes.auth_service.dto.response;

import lombok.*;
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
}
