package com.schoolManagement.schoolManagement.Service.Impl;

import com.schoolManagement.schoolManagement.DTO.request.StudentCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.StudentUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.PageResponse;
import com.schoolManagement.schoolManagement.DTO.response.StudentResponse;
import com.schoolManagement.schoolManagement.Entity.Enums.Gender;
import com.schoolManagement.schoolManagement.Entity.Student;
import com.schoolManagement.schoolManagement.Exception.DuplicateResourceException;
import com.schoolManagement.schoolManagement.Exception.ResourceNotFoundException;
import com.schoolManagement.schoolManagement.Mapper.StudentMapper;
import com.schoolManagement.schoolManagement.Repository.StudentRepository;
import com.schoolManagement.schoolManagement.Service.StudentService;
import com.schoolManagement.schoolManagement.Specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponse createStudent(StudentCreateRequest request){
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Student with email " + request.getEmail() + " already exists");
        }
        Student student = studentMapper.toEntity(request);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toResponse(savedStudent);
    }

    @Override
    public StudentResponse getStudentById(Long id){
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Student",  "id", id));
        return studentMapper.toResponse(student);
    }

    @Override
    public PageResponse<StudentResponse> getAllStudents(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir,
            String search,
            Gender gender,
            Boolean isActive
    ){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Student> studentPage = studentRepository.findAll(
                StudentSpecification.filterStudents(search, gender, isActive), pageable);
        Page<StudentResponse> responsePage = studentPage.map(studentMapper::toResponse);
        return PageResponse.from(responsePage);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long id, StudentUpdateRequest request){
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Student",  "id", id));
        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(student.getEmail())) {
            if (studentRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Student with email " + request.getEmail() + " already exists");
            }
        }
        studentMapper.updateEntityFromRequest(request, student);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toResponse(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id){
        Student student = studentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Student",  "id", id));
        studentRepository.delete(student);
    }
}
