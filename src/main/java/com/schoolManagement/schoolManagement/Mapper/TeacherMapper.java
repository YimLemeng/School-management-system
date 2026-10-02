package com.schoolManagement.schoolManagement.Mapper;

import com.schoolManagement.schoolManagement.DTO.request.TeacherCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.TeacherUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.TeacherResponse;
import com.schoolManagement.schoolManagement.Entity.Department;
import com.schoolManagement.schoolManagement.Entity.Teacher;
import com.schoolManagement.schoolManagement.Entity.TeacherProfile;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    public TeacherResponse toResponse(Teacher entity) {
        if (entity == null) return null;
        TeacherProfile profile = entity.getProfile();
        return TeacherResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .gender(entity.getGender())
                .departmentName(entity.getDepartment() != null ? entity.getDepartment().getName() : null)
                .qualification(profile != null ? profile.getQualification() : null)
                .yearsOfExperience(profile != null ? profile.getYearsOfExperience() : null)
                .emergencyPhone(profile != null ? profile.getEmergencyPhone() : null)
                .bio(profile != null ? profile.getBio() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Teacher toEntity(TeacherCreateRequest request, Department department) {
        if (request == null) return null;
        return Teacher.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .department(department)
                .profile(TeacherProfile.builder()
                        .qualification(request.getQualification())
                        .yearsOfExperience(request.getYearsOfExperience())
                        .emergencyPhone(request.getEmergencyPhone())
                        .bio(request.getBio())
                        .build())
                .build();

    }

    public void updateEntityFromRequest(TeacherUpdateRequest request, Teacher entity, Department department) {
        if (request == null || entity == null) return;
        if (request.getFirstName() != null) entity.setFirstName(request.getFirstName());
        if (request.getLastName() != null) entity.setLastName(request.getLastName());
        if (request.getEmail() != null) entity.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) entity.setPhoneNumber(request.getPhoneNumber());
        if (request.getGender() != null) entity.setGender(request.getGender());
        if (department != null) entity.setDepartment(department);

        TeacherProfile profile = entity.getProfile();
        if (profile == null) {
            profile = new TeacherProfile();
            entity.setProfile(profile);
        }
        if(request.getQualification() != null) profile.setQualification(request.getQualification());
        if (request.getYearsOfExperience() != null) profile.setYearsOfExperience(request.getYearsOfExperience());
        if (request.getEmergencyPhone() != null) profile.setEmergencyPhone(request.getEmergencyPhone());
        if (request.getBio() != null) profile.setBio(request.getBio());
    }
}
