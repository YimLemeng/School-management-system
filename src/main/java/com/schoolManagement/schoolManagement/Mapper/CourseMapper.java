package com.schoolManagement.schoolManagement.Mapper;

import com.schoolManagement.schoolManagement.DTO.request.CourseRequest;
import com.schoolManagement.schoolManagement.DTO.response.CourseResponse;
import com.schoolManagement.schoolManagement.Entity.Course;
import com.schoolManagement.schoolManagement.Entity.SubjectCategory;
import com.schoolManagement.schoolManagement.Entity.Teacher;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CourseMapper {
    public Course toEntity(CourseRequest request, Teacher teacher, Set<SubjectCategory> categories) {
        if (request == null) return null;
        return Course.builder()
                .title(request.getTitle())
                .code(request.getCode())
                .description(request.getDescription())
                .credit(request.getCredit())
                .fee(request.getFee())
                .capacity(request.getCapacity())
                .availableSeats(request.getCapacity())
                .teacher(teacher)
                .subjectCategories(categories != null ? categories : Set.of())
                .build();
    }

    public CourseResponse toResponse(Course entity) {
        if (entity == null) return null;
        Teacher teacher = entity.getTeacher();
        String teacherName = teacher != null ?  teacher.getFirstName() + " " + teacher.getLastName() : null;
        Set<String> categoryName = entity.getSubjectCategories() != null ? entity.getSubjectCategories()
                .stream()
                .map(SubjectCategory::getName)
                .collect(Collectors.toSet()) : Set.of();

        return CourseResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .code(entity.getCode())
                .description(entity.getDescription())
                .credit(entity.getCredit())
                .fee(entity.getFee())
                .capacity(entity.getCapacity())
                .availableSeats(entity.getAvailableSeats())
                .teacherId(teacher != null ? teacher.getId() : null)
                .teacherName(teacherName)
                .categories(categoryName)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
