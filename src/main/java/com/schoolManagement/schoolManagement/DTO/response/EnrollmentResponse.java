package com.schoolManagement.schoolManagement.DTO.response;

import com.schoolManagement.schoolManagement.Entity.Enums.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long id;
    private Long studentId;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseTitle;
    private String courseCode;
    private LocalDateTime enrollmentDate;
    private BigDecimal originalFee;
    private BigDecimal discountAmount;
    private BigDecimal finalFee;
    private EnrollmentStatus status;
    private String note;
}
