package com.schoolManagement.schoolManagement.DTO.request;

import com.schoolManagement.schoolManagement.Entity.Enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCreateRequest {

    @NotBlank(message = "Teacher firstname is required")
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Teacher lastname is required")
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    private String phoneNumber;
    private Gender gender;
    private Long departmentId;

    private String qualification;
    private Integer yearsOfExperience;
    private String emergencyPhone;
    private String bio;
}
