package com.petverse.app.service;

import com.petverse.app.dto.AuthResponse;
import com.petverse.app.dto.LoginRequest;
import com.petverse.app.dto.PetRequest;
import com.petverse.app.dto.RegisterRequest;
import com.petverse.app.dto.UpdateProfileRequest;
import com.petverse.app.entity.Pet;
import com.petverse.app.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    User register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    String refreshToken(String refreshToken);
}


