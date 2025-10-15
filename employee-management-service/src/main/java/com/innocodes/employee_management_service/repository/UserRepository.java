package com.innocodes.employee_management_service.repository;



import com.innocodes.employee_management_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByDepartmentId(Long departmentId);
}
