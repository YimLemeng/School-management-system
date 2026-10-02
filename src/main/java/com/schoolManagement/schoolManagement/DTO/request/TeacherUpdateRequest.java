package com.schoolManagement.schoolManagement.DTO.request;

import com.schoolManagement.schoolManagement.Entity.Enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherUpdateRequest {

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    @Email(message = "Email format is invalid")
    private String email;
    private String phoneNumber;
    private Gender gender;
    private Long departmentId;

    private String qualification;
    private Integer yearsOfExperience;
    private String emergencyPhone;
    private String bio;
}
