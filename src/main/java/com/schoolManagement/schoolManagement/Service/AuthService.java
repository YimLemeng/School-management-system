package com.schoolManagement.schoolManagement.Service;

import com.schoolManagement.schoolManagement.DTO.auth.*;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    UserProfileResponse getCurrentUser();
    void changePassword(ChangePasswordRequest request);
}
