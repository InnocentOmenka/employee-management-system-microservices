package com.innocodes.employee_management_service.dto;


import com.innocodes.employee_management_service.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private String managerEmail;
    private LocalDateTime createdAt;

    public DepartmentResponse(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.description = department.getDescription();
        this.managerEmail = department.getManagerEmail();
        this.createdAt = department.getCreatedAt();
    }

}
