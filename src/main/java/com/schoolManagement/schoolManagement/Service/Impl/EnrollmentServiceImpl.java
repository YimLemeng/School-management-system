package com.schoolManagement.schoolManagement.Service.Impl;

import com.schoolManagement.schoolManagement.DTO.request.EnrollmentRequest;
import com.schoolManagement.schoolManagement.DTO.response.EnrollmentResponse;
import com.schoolManagement.schoolManagement.Entity.Course;
import com.schoolManagement.schoolManagement.Entity.Enrollment;
import com.schoolManagement.schoolManagement.Entity.Enums.EnrollmentStatus;
import com.schoolManagement.schoolManagement.Entity.Student;
import com.schoolManagement.schoolManagement.Exception.BadRequestException;
import com.schoolManagement.schoolManagement.Exception.DuplicateResourceException;
import com.schoolManagement.schoolManagement.Exception.InsufficientCapacityException;
import com.schoolManagement.schoolManagement.Exception.ResourceNotFoundException;
import com.schoolManagement.schoolManagement.Mapper.EnrollmentMapper;
import com.schoolManagement.schoolManagement.Repository.CourseRepository;
import com.schoolManagement.schoolManagement.Repository.EnrollmentRepository;
import com.schoolManagement.schoolManagement.Repository.StudentRepository;
import com.schoolManagement.schoolManagement.Service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse EnrollmentStudent(EnrollmentRequest request){
        Student student = studentRepository.findById(request.getStudentId()).orElseThrow(() ->
                new ResourceNotFoundException("Student",  "id", request.getStudentId()));
        if (Boolean.FALSE.equals(student.getIsActive())){
            throw new BadRequestException("Inactive student cannot enroll in courses. Student ID: " + request.getStudentId());
        }
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() ->
                new ResourceNotFoundException("Course",  "id", request.getCourseId()));
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())){
            throw new DuplicateResourceException(
                    String.format("Student '%s %s' is already enrollment in course '%s'",
                            student.getFirstName(), student.getLastName(), course.getTitle()));
        }
        if (course.getAvailableSeats() <= 0){
            throw new InsufficientCapacityException(
                    String.format("Course '%s' (%s) has no available seats remaining (Capacity: %d)",
                            course.getTitle(), course.getCode(), course.getCapacity()));
        }
        BigDecimal originalFee = course.getFee();
        BigDecimal discountPercent = request.getDiscountPercentage() != null ? request.getDiscountPercentage() : BigDecimal.ZERO;

        BigDecimal discountAmount = originalFee.multiply(discountPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal finalFee = originalFee.subtract(discountAmount);

        course.setAvailableSeats(course.getAvailableSeats() - 1);
        courseRepository.save(course);
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollmentDate(LocalDateTime.now())
                .originalFee(originalFee)
                .discountAmount(discountAmount)
                .finalFee(finalFee)
                .status(EnrollmentStatus.CONFIRMED)
                .note(request.getNote())
                .build();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(savedEnrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponse cancelEnrollment(Long enrollmentId){
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() ->
                new ResourceNotFoundException("Enrollment",  "id", enrollmentId));
        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED){
            throw new BadRequestException("Enrollment is already cancelled");
        }
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        Course course = enrollment.getCourse();
        course.setAvailableSeats(course.getAvailableSeats() + 1);
        courseRepository.save(course);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(updatedEnrollment);
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentByStudent(Long studentId){
        if(!studentRepository.existsById(studentId)){
            throw new ResourceNotFoundException("Student",  "id", studentId);
        }
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentByCourse(Long courseId){
        if(!courseRepository.existsById(courseId)){
            throw new ResourceNotFoundException("Course",  "id", courseId);
        }
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(enrollmentMapper::toResponse).collect(Collectors.toList());
    }
}
