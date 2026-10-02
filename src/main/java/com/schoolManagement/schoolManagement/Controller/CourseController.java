package com.schoolManagement.schoolManagement.Controller;

import com.schoolManagement.schoolManagement.DTO.request.CourseRequest;
import com.schoolManagement.schoolManagement.DTO.response.ApiResponse;
import com.schoolManagement.schoolManagement.DTO.response.CourseResponse;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;
import com.schoolManagement.schoolManagement.Service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<CourseResponse>> addCourse(@Valid @RequestBody CourseRequest request){
        CourseResponse courseResponse = courseService.addCourse(request);
        return new ResponseEntity<>(ApiResponse.success("Course added successfully", courseResponse), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourseById(@PathVariable Long id){
        CourseResponse response = courseService.getCourseById(id);
        return ResponseEntity.ok(ApiResponse.success("Course found successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponse>>> getAllCourses(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int PageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false)BigDecimal maxFee,
            @RequestParam(required = false) Boolean availableOnly
    ){
        PageResponse<CourseResponse> response = courseService.getAllCourses(
                pageNo, PageSize, sortBy, sortDir, search, teacherId, maxFee, availableOnly);
        return ResponseEntity.ok(ApiResponse.success("Course found successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id){
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully"));
    }
}
