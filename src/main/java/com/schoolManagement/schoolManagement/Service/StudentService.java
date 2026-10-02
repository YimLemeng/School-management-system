package com.schoolManagement.schoolManagement.Service;

import com.schoolManagement.schoolManagement.DTO.request.StudentCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.StudentUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;
import com.schoolManagement.schoolManagement.DTO.response.StudentResponse;
import com.schoolManagement.schoolManagement.Entity.Enums.Gender;

public interface StudentService {
    StudentResponse createStudent(StudentCreateRequest request);
    StudentResponse getStudentById(Long id);
    PageResponse<StudentResponse> getAllStudents(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            String search,
            Gender gender,
            Boolean isActive);
    StudentResponse updateStudent(Long id, StudentUpdateRequest request);
    void deleteStudent(Long id);
}
