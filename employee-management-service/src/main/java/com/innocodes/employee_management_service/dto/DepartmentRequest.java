package com.innocodes.employee_management_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {
    private String name;
    private String description;
    private String managerEmail; // optional, for manager assignment
}
