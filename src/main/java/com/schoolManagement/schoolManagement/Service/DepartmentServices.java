package com.schoolManagement.schoolManagement.Service;

import com.schoolManagement.schoolManagement.DTO.request.DepartmentCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.DepartmentUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.DepartmentResponse;

import java.util.List;

public interface DepartmentServices {
    DepartmentResponse createDepartment(DepartmentCreateRequest request);
    DepartmentResponse getById(Long id);
    List<DepartmentResponse> getAllDepartments();
    DepartmentResponse updateDepartment(Long id, DepartmentUpdateRequest request);
    void deleteDepartment(Long id);
}
