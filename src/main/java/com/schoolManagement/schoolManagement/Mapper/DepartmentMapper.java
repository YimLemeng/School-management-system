package com.schoolManagement.schoolManagement.Mapper;

import com.schoolManagement.schoolManagement.DTO.request.DepartmentCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.DepartmentUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.DepartmentResponse;
import com.schoolManagement.schoolManagement.Entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentResponse toResponse(Department entity) {
        if (entity == null) return null;
        return DepartmentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .totalTeacher(entity.getTeachers() != null ? entity.getTeachers().size() : 0)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Department toEntity(DepartmentCreateRequest request) {
        if (request == null) return null;
        return Department.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .build();
    }

    public void updatedEntityFromRequest(DepartmentUpdateRequest request,  Department entity) {
        if (request == null || entity == null) return;
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getCode() != null) entity.setCode(request.getCode());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
    }
}
