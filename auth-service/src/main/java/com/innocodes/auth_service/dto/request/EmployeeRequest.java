package com.innocodes.auth_service.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String status;          // ACTIVE, INACTIVE
    private Long departmentId;      // reference to department
}
