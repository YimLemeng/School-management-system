package com.schoolManagement.schoolManagement.Service;

import com.schoolManagement.schoolManagement.DTO.request.CourseRequest;
import com.schoolManagement.schoolManagement.DTO.response.CourseResponse;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;

import java.math.BigDecimal;

public interface CourseService {
    CourseResponse addCourse(CourseRequest request);
    CourseResponse getCourseById(Long id);
    PageResponse<CourseResponse> getAllCourses(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            String search,
            Long teacherId,
            BigDecimal maxFee,
            Boolean availableOnly
    );
    void deleteCourse(Long id);
}
