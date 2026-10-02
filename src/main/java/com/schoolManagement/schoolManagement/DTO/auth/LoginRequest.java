package com.schoolManagement.schoolManagement.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Username or Email is requires")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;
}
