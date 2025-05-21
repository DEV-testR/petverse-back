package com.petverse.app.service.impl;

import com.petverse.app.dto.AuthResponse;
import com.petverse.app.dto.LoginRequest;
import com.petverse.app.dto.RegisterRequest;
import com.petverse.app.entity.User;
import com.petverse.app.repository.UserRepository;
import com.petverse.app.security.jwt.JwtTokenProvider;
import com.petverse.app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setSocialProvider(request.getSocialProvider());
        user.setSocialId(request.getSocialId());

        return userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // authenticate user; จะโยน exception ถ้าไม่ผ่าน
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String email = user.getEmail();
        String accessToken = jwtTokenProvider.generateAccessToken(email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email);

        return AuthResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Override
    public String refreshToken(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        return jwtTokenProvider.generateAccessToken(email);
    }
}
