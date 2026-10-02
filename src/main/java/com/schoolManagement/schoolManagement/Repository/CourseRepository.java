package com.schoolManagement.schoolManagement.Repository;

import com.schoolManagement.schoolManagement.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByTeacherId(Long teacherId);
}
