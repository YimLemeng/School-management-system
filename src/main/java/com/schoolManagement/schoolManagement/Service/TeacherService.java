package com.schoolManagement.schoolManagement.Service;

import com.schoolManagement.schoolManagement.DTO.request.TeacherCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.TeacherUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;
import com.schoolManagement.schoolManagement.DTO.response.TeacherResponse;

public interface TeacherService {
    TeacherResponse createTeacher(TeacherCreateRequest request);
    TeacherResponse getById(Long id);
    PageResponse<TeacherResponse> getAllTeachers(int pageNo, int pageSize, String sortBy, String sortDir);
    TeacherResponse updateTeacher(Long id, TeacherUpdateRequest request);
    void deleteTeacher(Long id);
}
