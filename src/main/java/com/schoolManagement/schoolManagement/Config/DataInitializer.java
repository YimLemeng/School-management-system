package com.schoolManagement.schoolManagement.Config;

import com.schoolManagement.schoolManagement.Entity.*;
import com.schoolManagement.schoolManagement.Entity.Enums.Gender;
import com.schoolManagement.schoolManagement.Entity.Enums.Role;
import com.schoolManagement.schoolManagement.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectCategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        userRepository.findAll().forEach(user -> {
            if (user.getRoles().contains(Role.ROLE_STUDENT) && !studentRepository.existsByEmail(user.getEmail())) {
                String fullName = user.getFullName();
                String firstName = "Student";
                String lastName = user.getUsername();
                if (fullName != null && !fullName.trim().isEmpty()) {
                    String[] parts = fullName.trim().split("\\s+", 2);
                    firstName = parts[0];
                    lastName = parts.length > 1 ? parts[1] : parts[0];
                }
                studentRepository.save(Student.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(user.getEmail())
                        .gender(Gender.MALE)
                        .isActive(true)
                        .build());
                log.info("Synced student record for user: {}", user.getUsername());
            }
        });

        if (departmentRepository.count() > 0) {
            log.info("Database already seeded with initial data.");
            return;
        }
        log.info("Seeding initial data for School Management System...");

        Department csDept = Department.builder()
                .name("Computer Science")
                .code("CS")
                .description("Department of Computer Science & Software Engineering")
                .build();

        Department mathDept = Department.builder()
                .name("Mathematics")
                .code("MATH")
                .description("Department of Applied Mathematics and Statistics")
                .build();

        Department physicsDept = Department.builder()
                .name("Physics")
                .code("PHY")
                .description("Department of Physics and Astronomy")
                .build();
        departmentRepository.save(csDept);
        departmentRepository.save(mathDept);
        departmentRepository.save(physicsDept);

        Teacher teacher1 = Teacher.builder()
                .firstName("Sokha")
                .lastName("Chan")
                .email("sokha.chan@school.edu.kh")
                .phoneNumber("+85512998877")
                .gender(Gender.MALE)
                .department(csDept)
                .profile(TeacherProfile.builder()
                        .qualification("Ph.D. in Computer Science")
                        .yearsOfExperience(8)
                        .emergencyPhone("+85512345678")
                        .bio("Specialist in Java Enterprise Development and Distributed Systems.")
                        .build())
                .build();

        Teacher teacher2 = Teacher.builder()
                .firstName("Rithy")
                .lastName("Vong")
                .email("rithy.vong@school.edu.kh")
                .phoneNumber("+85512887766")
                .gender(Gender.MALE)
                .department(csDept)
                .profile(TeacherProfile.builder()
                        .qualification("Master of Software Engineering")
                        .yearsOfExperience(5)
                        .emergencyPhone("+85512345679")
                        .bio("Expert in Cloud Computing, AWS, and DevOps practices.")
                        .build())
                .build();

        Teacher teacher3 = Teacher.builder()
                .firstName("Srey Pov")
                .lastName("Chea")
                .email("sreypov.chea@school.edu.kh")
                .phoneNumber("+85512776655")
                .gender(Gender.FEMALE)
                .department(csDept)
                .profile(TeacherProfile.builder()
                        .qualification("Ph.D. in Artificial Intelligence")
                        .yearsOfExperience(7)
                        .emergencyPhone("+85512345680")
                        .bio("Passionate about Machine Learning, Data Science, and Python.")
                        .build())
                .build();

        Teacher teacher4 = Teacher.builder()
                .firstName("Vicheka")
                .lastName("Hout")
                .email("vicheka.hout@school.edu.kh")
                .phoneNumber("+85512665544")
                .gender(Gender.MALE)
                .department(mathDept)
                .profile(TeacherProfile.builder()
                        .qualification("Ph.D. in Applied Mathematics")
                        .yearsOfExperience(12)
                        .emergencyPhone("+85512345681")
                        .bio("Specialist in Linear Algebra and Numerical Analysis.")
                        .build())
                .build();

        Teacher teacher5 = Teacher.builder()
                .firstName("Chanthy")
                .lastName("Sorn")
                .email("chanthy.sorn@school.edu.kh")
                .phoneNumber("+85512554433")
                .gender(Gender.FEMALE)
                .department(mathDept)
                .profile(TeacherProfile.builder()
                        .qualification("Master of Statistics")
                        .yearsOfExperience(6)
                        .emergencyPhone("+85512345682")
                        .bio("Focuses on Probability, Data Statistics, and R programming.")
                        .build())
                .build();

        Teacher teacher6 = Teacher.builder()
                .firstName("Dara")
                .lastName("Panha")
                .email("dara.panha@school.edu.kh")
                .phoneNumber("+85512443322")
                .gender(Gender.MALE)
                .department(physicsDept)
                .profile(TeacherProfile.builder()
                        .qualification("Ph.D. in Theoretical Physics")
                        .yearsOfExperience(10)
                        .emergencyPhone("+85512345683")
                        .bio("Researching quantum mechanics and modern physics.")
                        .build())
                .build();

        Teacher teacher7 = Teacher.builder()
                .firstName("Kalyan")
                .lastName("Tep")
                .email("kalyan.tep@school.edu.kh")
                .phoneNumber("+85512332211")
                .gender(Gender.FEMALE)
                .department(csDept)
                .profile(TeacherProfile.builder()
                        .qualification("Master of Cybersecurity")
                        .yearsOfExperience(4)
                        .emergencyPhone("+85512345684")
                        .bio("Focuses on Network Security, Cryptography, and Ethical Hacking.")
                        .build())
                .build();

        Teacher teacher8 = Teacher.builder()
                .firstName("Borey")
                .lastName("Kim")
                .email("borey.kim@school.edu.kh")
                .phoneNumber("+85512221100")
                .gender(Gender.MALE)
                .department(csDept)
                .profile(TeacherProfile.builder()
                        .qualification("Bachelor of Computer Science")
                        .yearsOfExperience(3)
                        .emergencyPhone("+85512345685")
                        .bio("Mobile app development specialist (Flutter & React Native).")
                        .build())
                .build();

        Teacher teacher9 = Teacher.builder()
                .firstName("Sophea")
                .lastName("Lim")
                .email("sophea.lim@school.edu.kh")
                .phoneNumber("+85512110099")
                .gender(Gender.FEMALE)
                .department(mathDept)
                .profile(TeacherProfile.builder()
                        .qualification("Master of Mathematics Education")
                        .yearsOfExperience(9)
                        .emergencyPhone("+85512345686")
                        .bio("Calculus and Discrete Mathematics instructor.")
                        .build())
                .build();

        Teacher teacher10 = Teacher.builder()
                .firstName("Piseth")
                .lastName("Nhem")
                .email("piseth.nhem@school.edu.kh")
                .phoneNumber("+85512009988")
                .gender(Gender.MALE)
                .department(physicsDept)
                .profile(TeacherProfile.builder()
                        .qualification("Master of Physics")
                        .yearsOfExperience(5)
                        .emergencyPhone("+85512345687")
                        .bio("Laboratory supervisor and experimental physics expert.")
                        .build())
                .build();
        teacherRepository.saveAll(List.of(
                teacher1, teacher2, teacher3, teacher4, teacher5,
                teacher6, teacher7, teacher8, teacher9, teacher10));

        SubjectCategory catJava = categoryRepository.save(SubjectCategory.builder().name("Programming").description("Software engineering and programming languages").build());
        SubjectCategory catBackend = categoryRepository.save(SubjectCategory.builder().name("Backend Architecture").description("Server-side APIs, database management").build());
        SubjectCategory catAI = categoryRepository.save(SubjectCategory.builder().name("Artificial Intelligence").description("Machine learning and neural networks").build());
        SubjectCategory catMath = categoryRepository.save(SubjectCategory.builder().name("Calculus").description("Advanced mathematical concepts").build());

        Course course1 = Course.builder()
                .title("Advanced Spring Boot 3 & Clean Architecture")
                .code("CS301")
                .description("Comprehensive guide to enterprise Spring Boot microservices and clean code.")
                .credit(4)
                .fee(new BigDecimal("250.00"))
                .capacity(30)
                .availableSeats(30)
                .teacher(teacher1)
                .subjectCategories(Set.of(catJava, catBackend))
                .build();

        Course course2 = Course.builder()
                .title("Database Systems & PostgreSQL Optimization")
                .code("CS202")
                .description("Deep dive into relational schema design and SQL query performance.")
                .credit(3)
                .fee(new BigDecimal("180.00"))
                .capacity(30)
                .availableSeats(30)
                .teacher(teacher2)
                .subjectCategories(Set.of(catBackend))
                .build();

        Course course3 = Course.builder()
                .title("Machine Learning with Python")
                .code("CS401")
                .description("Introduction to machine learning algorithms using Python and Scikit-Learn.")
                .credit(4)
                .fee(new BigDecimal("300.00"))
                .capacity(25)
                .availableSeats(25)
                .teacher(teacher3)
                .subjectCategories(Set.of(catAI))
                .build();

        Course course4 = Course.builder()
                .title("Linear Algebra for Computer Science")
                .code("MATH101")
                .description("Vectors, matrices, and transformations applied in CS.")
                .credit(3)
                .fee(new BigDecimal("150.00"))
                .capacity(40)
                .availableSeats(40)
                .teacher(teacher4)
                .subjectCategories(Set.of(catMath))
                .build();

        Course course5 = Course.builder()
                .title("Applied Probability & Statistics")
                .code("MATH202")
                .description("Probability distributions, hypothesis testing, and data analysis with R.")
                .credit(3)
                .fee(new BigDecimal("170.00"))
                .capacity(35)
                .availableSeats(35)
                .teacher(teacher5)
                .subjectCategories(Set.of(catMath))
                .build();

        Course course6 = Course.builder()
                .title("General Physics: Mechanics & Thermodynamics")
                .code("PHY101")
                .description("Fundamental laws of motion, energy, heat, and thermodynamics.")
                .credit(4)
                .fee(new BigDecimal("200.00"))
                .capacity(35)
                .availableSeats(35)
                .teacher(teacher6)
                .subjectCategories(Set.of(catMath))
                .build();

        Course course7 = Course.builder()
                .title("Network Security & Ethical Hacking")
                .code("CS305")
                .description("Securing networks, cryptography principles, and penetration testing.")
                .credit(3)
                .fee(new BigDecimal("220.00"))
                .capacity(25)
                .availableSeats(25)
                .teacher(teacher7)
                .subjectCategories(Set.of(catBackend))
                .build();

        Course course8 = Course.builder()
                .title("Cross-Platform Mobile App Development")
                .code("CS205")
                .description("Building native iOS and Android applications using Flutter and Dart.")
                .credit(3)
                .fee(new BigDecimal("210.00"))
                .capacity(30)
                .availableSeats(30)
                .teacher(teacher8)
                .subjectCategories(Set.of(catJava))
                .build();

        Course course9 = Course.builder()
                .title("Discrete Mathematics & Calculus")
                .code("MATH301")
                .description("Logic, set theory, graphs, and advanced calculus for engineering.")
                .credit(4)
                .fee(new BigDecimal("190.00"))
                .capacity(35)
                .availableSeats(35)
                .teacher(teacher9)
                .subjectCategories(Set.of(catMath))
                .build();

        Course course10 = Course.builder()
                .title("Modern Physics & Quantum Mechanics")
                .code("PHY202")
                .description("Introduction to quantum theory, wave-particle duality, and atomic physics.")
                .credit(4)
                .fee(new BigDecimal("240.00"))
                .capacity(20)
                .availableSeats(20)
                .teacher(teacher10)
                .subjectCategories(Set.of(catMath))
                .build();
        courseRepository.saveAll(List.of(
                course1, course2, course3, course4, course5,
                course6, course7, course8, course9, course10));

        List<Student> students = new ArrayList<>();
        String[] firstNames = {
                "Vichheka", "Bopha", "Dara", "Sreymom", "Chanrith",
                "Sothea", "Kanal", "Raksmey", "Sophea", "Visal",
                "Kimleng", "Sokun", "Vatana", "Rithisak", "Kanika",
                "Mony", "Chanthou", "Sokcheat", "Chenda", "Panha",
                "Tola", "Nary", "Sreysros", "Phearak", "Sreyneath",
                "Kosal", "Vanna", "Sreyroth", "Sokunthea", "Darith",
                "Sreyleak", "Channary", "Vicheth", "Sophorn", "Sokuntheary"
        };
        String[] lastNames = {
                "Keo", "Meas", "Sok", "Chea", "Chan",
                "Hout", "Sorn", "Panha", "Tep", "Kim",
                "Lim", "Nhem", "Vong", "Srey", "Sam",
                "Pheng", "Oum", "Luy", "Soeur", "Chhorn",
                "Yim", "Ung", "Mao", "Bun", "Kov",
                "Seng", "Khieu", "Pich", "Ros", "Nou",
                "Sim", "Thy", "Cheung", "Lay", "Eang"
        };

        for (int i = 0; i < 35; i++) {
            students.add(Student.builder()
                    .firstName(firstNames[i])
                    .lastName(lastNames[i])
                    .email((firstNames[i] + "." + lastNames[i] + (i + 1) + "@gmail.com").toLowerCase())
                    .phoneNumber(String.format("+85510%07d", 1000000 + i * 1234))
                    .dateOfBirth(LocalDate.of(2000 + (i % 4), (i % 12) + 1, (i % 28) + 1))
                    .gender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE)
                    .isActive(true)
                    .build());
        }
        studentRepository.saveAll(students);
        log.info("Initial data seeding completed successfully with 10 teachers!");
    }
}
