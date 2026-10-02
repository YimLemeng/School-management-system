package com.schoolManagement.schoolManagement.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {
    private Long id;
    private String title;
    private String code;
    private String description;
    private Integer credit;
    private BigDecimal fee;
    private Integer capacity;
    private Integer availableSeats;
    private Long teacherId;
    private String teacherName;
    private Set<String> categories;
    private LocalDateTime createdAt;
}
