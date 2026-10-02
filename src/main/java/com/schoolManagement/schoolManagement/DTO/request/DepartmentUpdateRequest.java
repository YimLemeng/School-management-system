package com.schoolManagement.schoolManagement.DTO.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentUpdateRequest {

    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    @Size(min = 2, max = 20, message = "Department code must be between 2 and 20 characters")
    private String code;

    private String description;
}
