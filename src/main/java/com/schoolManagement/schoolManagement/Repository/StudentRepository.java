package com.schoolManagement.schoolManagement.Repository;

import com.schoolManagement.schoolManagement.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
}
