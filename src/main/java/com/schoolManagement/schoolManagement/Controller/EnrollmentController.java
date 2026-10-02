package com.schoolManagement.schoolManagement.Controller;

import com.schoolManagement.schoolManagement.DTO.request.EnrollmentRequest;
import com.schoolManagement.schoolManagement.DTO.response.ApiResponse;
import com.schoolManagement.schoolManagement.DTO.response.EnrollmentResponse;
import com.schoolManagement.schoolManagement.Service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollmentStudent(@Valid @RequestBody EnrollmentRequest request){
        EnrollmentResponse response = enrollmentService.EnrollmentStudent(request);
        return new ResponseEntity<>(ApiResponse.success("Student enroll successfully", response), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}/cancel", method = {RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> cancelEnrollment(@PathVariable Long id){
        EnrollmentResponse response = enrollmentService.cancelEnrollment(id);
        return ResponseEntity.ok(ApiResponse.success("Enrollment cancelled successfully", response));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getEnrollmentByStudent(@PathVariable Long studentId){
        List<EnrollmentResponse> response = enrollmentService.getEnrollmentByStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Student enrollments retrieved successfully", response));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getEnrollmentByCourse(@PathVariable Long courseId){
        List<EnrollmentResponse> response = enrollmentService.getEnrollmentByCourse(courseId);
        return ResponseEntity.ok(ApiResponse.success("Course enrollments retrieved successfully", response));
    }
}
