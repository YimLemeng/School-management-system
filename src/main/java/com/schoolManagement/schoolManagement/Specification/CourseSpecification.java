package com.schoolManagement.schoolManagement.Specification;

import com.schoolManagement.schoolManagement.Entity.Course;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class CourseSpecification {
    public static Specification<Course> filterCourses(
            String search,
            Long teacherId,
            BigDecimal maxFee,
            Boolean availableOnly
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.hasText(search)) {
                String searchPattern = "%" + search.trim().toLowerCase() + "%";
                Predicate titleMatch = cb.like(cb.lower(root.get("title")), searchPattern);
                Predicate codeMatch = cb.like(cb.lower(root.get("code")), searchPattern);
                predicates.add(cb.or(titleMatch, codeMatch));
            }
            if (teacherId != null) {
                predicates.add(cb.equal(root.get("teacher").get("id"), teacherId));
            }
            if(maxFee != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fee"), maxFee));
            }
            if (Boolean.TRUE.equals(availableOnly)) {
                predicates.add(cb.greaterThan(root.get("availableSeats"), 0));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
