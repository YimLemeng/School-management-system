package com.schoolManagement.schoolManagement.Controller;

import com.schoolManagement.schoolManagement.DTO.request.TeacherCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.TeacherUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.ApiResponse;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;
import com.schoolManagement.schoolManagement.DTO.response.TeacherResponse;
import com.schoolManagement.schoolManagement.Service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> createTeacher(@Valid @RequestBody TeacherCreateRequest request){
        TeacherResponse response = teacherService.createTeacher(request);
        return new ResponseEntity<>(ApiResponse.success("Teacher create successfully", response),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> getTeacher(@PathVariable Long id){
        TeacherResponse response = teacherService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Teacher get successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<TeacherResponse>>> getAllTeachers(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        PageResponse<TeacherResponse> response = teacherService.getAllTeachers(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success("Teacher retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponse>> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherUpdateRequest request){
        TeacherResponse response = teacherService.updateTeacher(id, request);
        return ResponseEntity.ok(ApiResponse.success("Teacher update successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long id){
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.success("Teacher deleted successfully"));
    }
}
