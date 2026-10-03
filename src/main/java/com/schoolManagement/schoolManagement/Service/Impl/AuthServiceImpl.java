package com.schoolManagement.schoolManagement.Service.Impl;

import com.schoolManagement.schoolManagement.DTO.auth.*;
import com.schoolManagement.schoolManagement.Entity.Enums.Gender;
import com.schoolManagement.schoolManagement.Entity.Enums.Role;
import com.schoolManagement.schoolManagement.Entity.Student;
import com.schoolManagement.schoolManagement.Entity.User;
import com.schoolManagement.schoolManagement.Exception.BadRequestException;
import com.schoolManagement.schoolManagement.Exception.DuplicateResourceException;
import com.schoolManagement.schoolManagement.Exception.ResourceNotFoundException;
import com.schoolManagement.schoolManagement.Repository.StudentRepository;
import com.schoolManagement.schoolManagement.Repository.UserRepository;
import com.schoolManagement.schoolManagement.Security.UserPrincipal;
import com.schoolManagement.schoolManagement.Security.jwt.JwtTokenProvider;
import com.schoolManagement.schoolManagement.Service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Register new user with username: {}", request.getUsername());
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password and Confirm Password do not match.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' already registered.");
        }
        Set<Role> roles = new HashSet<>();
        if(request.getRoles()!=null && !request.getRoles().isEmpty()) {
            roles.addAll(request.getRoles());
        } else {
            roles.add(Role.ROLE_STUDENT);
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .roles(roles)
                .isActive(true)
                .build();
        User savedUser = userRepository.save(user);

        if (roles.contains(Role.ROLE_STUDENT)) {
            createStudentForUser(savedUser);
        }

        List<String> roleNames = savedUser.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toList());
        String token = jwtTokenProvider.generateTokenFromUsername(savedUser.getUsername(), roleNames);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationInMs())
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .roles(new HashSet<>(roleNames))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Authenticating user: {}", request.getUsernameOrEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail().trim(),
                        request.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Set<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        if (roles.contains("ROLE_STUDENT") || roles.contains(Role.ROLE_STUDENT.name())) {
            userRepository.findById(userPrincipal.getId()).ifPresent(this::createStudentForUser);
        }

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getJwtExpirationInMs())
                .userId(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .email(userPrincipal.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public UserProfileResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("No authenticated user found in SecurityContext.");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username));

        if (user.getRoles().contains(Role.ROLE_STUDENT)) {
            createStudentForUser(user);
        }

        Set<String> roleNames = user.getRoles().stream().map(Role::name).collect(Collectors.toSet());
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .roles(roleNames)
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private Student createStudentForUser(User user) {
        if (studentRepository.existsByEmail(user.getEmail())) {
            return studentRepository.findByEmail(user.getEmail()).orElse(null);
        }
        String fullName = user.getFullName();
        String firstName = "Student";
        String lastName = user.getUsername();
        if (fullName != null && !fullName.trim().isEmpty()) {
            String[] parts = fullName.trim().split("\\s+", 2);
            firstName = parts[0];
            lastName = parts.length > 1 ? parts[1] : "";
        }
        Student student = Student.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(user.getEmail())
                .gender(Gender.MALE)
                .isActive(true)
                .build();
        log.info("Auto-provisioned Student record for user {}: {} {}", user.getUsername(), firstName, lastName);
        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username));

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new BadRequestException("Password and Confirm New Password do not match.");
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current Password and Confirm New Password do not match.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password updated successfully for user: {}", username);
    }
}
