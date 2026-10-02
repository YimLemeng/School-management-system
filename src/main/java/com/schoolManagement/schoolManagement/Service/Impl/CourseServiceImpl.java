package com.schoolManagement.schoolManagement.Service.Impl;

import com.schoolManagement.schoolManagement.DTO.request.CourseRequest;
import com.schoolManagement.schoolManagement.DTO.response.CourseResponse;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;
import com.schoolManagement.schoolManagement.Entity.Course;
import com.schoolManagement.schoolManagement.Entity.SubjectCategory;
import com.schoolManagement.schoolManagement.Entity.Teacher;
import com.schoolManagement.schoolManagement.Exception.DuplicateResourceException;
import com.schoolManagement.schoolManagement.Exception.ResourceNotFoundException;
import com.schoolManagement.schoolManagement.Mapper.CourseMapper;
import com.schoolManagement.schoolManagement.Repository.CourseRepository;
import com.schoolManagement.schoolManagement.Repository.SubjectCategoryRepository;
import com.schoolManagement.schoolManagement.Repository.TeacherRepository;
import com.schoolManagement.schoolManagement.Service.CourseService;
import com.schoolManagement.schoolManagement.Specification.CourseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectCategoryRepository categoryRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseResponse addCourse(CourseRequest request) {
        if(courseRepository.existsByCode(request.getCode()))
            throw new DuplicateResourceException("Course with code " + request.getCode() + " already exists");
        Teacher teacher = null;
        if (request.getTeacherId() != null) {
            teacher = teacherRepository.findById(request.getTeacherId()).orElseThrow(() ->
                    new ResourceNotFoundException("Teacher", "id", request.getTeacherId()));
        }
        Set<SubjectCategory> categories = new HashSet<>();
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categories.addAll(categoryRepository.findAllById(request.getCategoryIds()));
        }

        Course course = courseMapper.toEntity(request, teacher, categories);
        Course addedCourse = courseRepository.save(course);
        return courseMapper.toResponse(addedCourse);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Course", "id", id));
        return courseMapper.toResponse(course);
    }

    @Override
    public PageResponse<CourseResponse> getAllCourses(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            String search,
            Long teacherId,
            BigDecimal maxFee,
            Boolean availableOnly
    ){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Course> coursePage = courseRepository.findAll(
                CourseSpecification.filterCourses(search, teacherId, maxFee, availableOnly), pageable
        );
        Page<CourseResponse> responsePage = coursePage.map(courseMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Course", "id", id));
        courseRepository.delete(course);
    }
}
