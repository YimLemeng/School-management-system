package com.schoolManagement.schoolManagement.Repository;

import com.schoolManagement.schoolManagement.Entity.SubjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectCategoryRepository extends JpaRepository<SubjectCategory, Long> {
    Optional<SubjectCategory> findByName(String name);
}
