package com.schoolManagement.schoolManagement.Service;

import com.schoolManagement.schoolManagement.DTO.request.EnrollmentRequest;
import com.schoolManagement.schoolManagement.DTO.response.EnrollmentResponse;

import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse EnrollmentStudent(EnrollmentRequest request);
    EnrollmentResponse cancelEnrollment(Long enrollmentId);
    List<EnrollmentResponse> getEnrollmentByStudent(Long studentId);
    List<EnrollmentResponse> getEnrollmentByCourse(Long courseId);
}
