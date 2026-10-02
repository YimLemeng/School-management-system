package com.schoolManagement.schoolManagement.Service.Impl;

import com.schoolManagement.schoolManagement.DTO.request.DepartmentCreateRequest;
import com.schoolManagement.schoolManagement.DTO.request.DepartmentUpdateRequest;
import com.schoolManagement.schoolManagement.DTO.response.DepartmentResponse;
import com.schoolManagement.schoolManagement.Entity.Department;
import com.schoolManagement.schoolManagement.Exception.BadRequestException;
import com.schoolManagement.schoolManagement.Exception.ResourceNotFoundException;
import com.schoolManagement.schoolManagement.Mapper.DepartmentMapper;
import com.schoolManagement.schoolManagement.Repository.DepartmentRepository;
import com.schoolManagement.schoolManagement.Service.DepartmentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentServices {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper mapper;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentCreateRequest request) {
        if (departmentRepository.existsByName(request.getName()))
            throw new BadRequestException("Department with name " + request.getName() + " already exists");
        Department entity = mapper.toEntity(request);
        Department savedDepartment = departmentRepository.save(entity);
        return mapper.toResponse(savedDepartment);
    }

    @Override
    public DepartmentResponse getById(Long id) {
        Department entity = departmentRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Department", "id", id));
        return mapper.toResponse(entity);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentUpdateRequest request) {
        Department entity = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        if (request.getName() != null && !request.getName().equalsIgnoreCase(entity.getName())) {
            if (departmentRepository.existsByName(request.getName()))
                throw new BadRequestException("Department with name " + request.getName() + " already exists");
        }
        mapper.updatedEntityFromRequest(request, entity);
        Department updatedDepartment = departmentRepository.save(entity);
        return mapper.toResponse(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        if (department.getTeachers() != null && !department.getTeachers().isEmpty())
            throw new BadRequestException(String.format("Cannot delete department '%s' because %d teacher(s) are currently assigned to it. Reassign or remove teachers first.",
                    department.getName(), department.getTeachers().size()));
        departmentRepository.delete(department);
    }
}
