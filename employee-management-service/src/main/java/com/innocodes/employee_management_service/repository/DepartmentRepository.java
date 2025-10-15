package com.innocodes.employee_management_service.repository;

import com.innocodes.employee_management_service.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByManagerEmail(String managerEmail);
}
