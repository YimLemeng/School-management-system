package com.schoolManagement.schoolManagement.Mapper;

import com.schoolManagement.schoolManagement.DTO.response.EnrollmentResponse;
import com.schoolManagement.schoolManagement.Entity.Course;
import com.schoolManagement.schoolManagement.Entity.Enrollment;
import com.schoolManagement.schoolManagement.Entity.Student;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    public EnrollmentResponse toResponse(Enrollment entity) {
        if(entity == null) return null;
        Student student = entity.getStudent();
        Course course = entity.getCourse();
        String studentName = student != null ? student.getFirstName() + " " + student.getLastName() : null;
        return EnrollmentResponse.builder()
                .id(entity.getId())
                .studentId(student != null ? student.getId() : null)
                .studentName(studentName)
                .studentEmail(student != null ? student.getEmail() : null)
                .courseId(course != null ? course.getId() : null)
                .courseTitle(course != null ? course.getTitle() : null)
                .courseCode(course != null ? course.getCode() : null)
                .enrollmentDate(entity.getEnrollmentDate())
                .originalFee(entity.getOriginalFee())
                .discountAmount(entity.getDiscountAmount())
                .finalFee(entity.getFinalFee())
                .status(entity.getStatus())
                .note(entity.getNote())
                .build();
    }
}
