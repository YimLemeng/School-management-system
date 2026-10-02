package com.schoolManagement.schoolManagement.DTO.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequest {

    @NotBlank(message = "Course title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;

    private String description;

    @NotNull(message = "Credit is required")
    @Min(value = 1, message = "Credit must be at least 1")
    @Max(value = 10, message = "Credit cannot exceed 10")
    private Integer credit;

    @NotNull(message = "Course fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Fee cannot be negative")
    private BigDecimal fee;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1 student")
    private Integer capacity;

    private Long teacherId;

    private Set<Long> categoryIds;
}
