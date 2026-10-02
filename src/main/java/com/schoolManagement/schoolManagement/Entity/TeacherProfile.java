package com.schoolManagement.schoolManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "teacher_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherProfile extends BaseEntity {

    @Column(length = 100)
    private String qualification;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "emergency_phone", length = 20)
    private String emergencyPhone;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
