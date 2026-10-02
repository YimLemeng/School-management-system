package com.schoolManagement.schoolManagement.DTO.response;

import com.schoolManagement.schoolManagement.Entity.Enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Gender gender;
    private String departmentName;
    private String qualification;
    private Integer yearsOfExperience;
    private String emergencyPhone;
    private String bio;
    private LocalDateTime createdAt;
}
