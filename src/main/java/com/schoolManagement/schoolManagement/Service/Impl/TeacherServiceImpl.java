package com.schoolManagement.schoolManagement.Service.Impl;

import com.schoolManagement.schoolManagement.DTO.request.TeacherCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.TeacherUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;
import com.schoolManagement.schoolManagement.DTO.response.TeacherResponse;
import com.schoolManagement.schoolManagement.Entity.Department;
import com.schoolManagement.schoolManagement.Entity.Teacher;
import com.schoolManagement.schoolManagement.Exception.BadRequestException;
import com.schoolManagement.schoolManagement.Exception.ResourceNotFoundException;
import com.schoolManagement.schoolManagement.Mapper.TeacherMapper;
import com.schoolManagement.schoolManagement.Repository.CourseRepository;
import com.schoolManagement.schoolManagement.Repository.DepartmentRepository;
import com.schoolManagement.schoolManagement.Repository.TeacherRepository;
import com.schoolManagement.schoolManagement.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final TeacherMapper mapper;

    @Override
    @Transactional
    public TeacherResponse createTeacher(TeacherCreateRequest request) {
        if (teacherRepository.existsByEmail(request.getEmail()))
            throw new BadRequestException("Teacher with email " + request.getEmail() + " already exists");
        Department department = null;
        if (request.getDepartmentId() != null)
            department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() ->
                    new ResourceNotFoundException("Department", "id", request.getDepartmentId()));
        Teacher teacher = mapper.toEntity(request, department);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return mapper.toResponse(savedTeacher);
    }

    @Override
    public TeacherResponse getById(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        return mapper.toResponse(teacher);
    }

    @Override
    public PageResponse<TeacherResponse> getAllTeachers(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Teacher> teacherPage = teacherRepository.findAll(pageable);
        Page<TeacherResponse> responsePage = teacherPage.map(mapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherUpdateRequest request) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(teacher.getEmail())) {
            if (teacherRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("Teacher with email " + request.getEmail() + " already exists");
            }
        }
        Department department = null;
        if (request.getDepartmentId() != null)
            department = departmentRepository.findById(request.getDepartmentId()).orElseThrow(() ->
                    new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        mapper.updateEntityFromRequest(request, teacher, department);
        Teacher updatedTeacher = teacherRepository.save(teacher);
        return mapper.toResponse(updatedTeacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Teacher", "id", id));
        if (courseRepository.existsByTeacherId(id)){
            throw new BadRequestException("Cannot delete teacher with id " + id + "because they teach course(s).");
        }
        teacherRepository.delete(teacher);
    }
}
