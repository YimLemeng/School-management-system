package com.schoolManagement.schoolManagement.Controller;

import com.schoolManagement.schoolManagement.DTO.request.DepartmentCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.DepartmentUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.ApiResponse;
import com.schoolManagement.schoolManagement.DTO.response.DepartmentResponse;
import com.schoolManagement.schoolManagement.Service.DepartmentServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentServices departmentServices;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        DepartmentResponse response = departmentServices.createDepartment(request);
        return new ResponseEntity<>(ApiResponse.success("Department create successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable Long id) {
        DepartmentResponse response = departmentServices.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAllDepartments() {
        List<DepartmentResponse> department =  departmentServices.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", department));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateRequest request) {
        DepartmentResponse department =  departmentServices.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", department));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentServices.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department has been deleted"));
    }
}
