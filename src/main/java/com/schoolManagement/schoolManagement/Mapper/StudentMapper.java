package com.schoolManagement.schoolManagement.Mapper;

import com.schoolManagement.schoolManagement.DTO.request.StudentCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.StudentUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.StudentResponse;
import com.schoolManagement.schoolManagement.Entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public Student toEntity(StudentCreateRequest request) {
        if (request == null) return null;
        return Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .build();
    }

    public void updateEntityFromRequest(StudentUpdateRequest request, Student entity) {
        if (request == null || entity == null) return;
        if (request.getFirstName() != null) entity.setFirstName(request.getFirstName());
        if (request.getLastName() != null) entity.setLastName(request.getLastName());
        if (request.getEmail() != null) entity.setEmail(request.getEmail());
        if (request.getPhoneNumber() != null) entity.setPhoneNumber(request.getPhoneNumber());
        if (request.getDateOfBirth() != null) entity.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) entity.setGender(request.getGender());
        if (request.getIsActive() != null) entity.setIsActive(request.getIsActive());
    }

    public StudentResponse toResponse(Student entity) {
        if (entity == null) return null;
        return StudentResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .dateOfBirth(entity.getDateOfBirth())
                .gender(entity.getGender())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
