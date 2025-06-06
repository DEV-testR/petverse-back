package com.petverse.app.controller;

import com.petverse.app.dto.AuthResponse;
import com.petverse.app.dto.LoginRequest;
import com.petverse.app.dto.RegisterRequest;
import com.petverse.app.entity.User;
import com.petverse.app.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    // 1. ลงทะเบียน
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // 2. ล็อกอิน (email/password หรือ social)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);
        long refreshTokenExpirationMs = authResponse.getRefreshTokenExpirationMs();
        // เซ็ต refresh token เป็น HttpOnly, Secure cookie
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(true)  // ใช้ true ใน production ที่มี HTTPS
                .path("/api/auth/refresh")
                .maxAge(refreshTokenExpirationMs)  // 7 วัน
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/login/pin")
    public ResponseEntity<AuthResponse> loginWithPin(@RequestParam String email, @RequestParam String pin) {
        AuthResponse authResponse = authService.loginWithPin(email, pin);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/email/validate")
    public ResponseEntity<Void> validateEmail(@RequestParam String email) {
        boolean isExistsMail = authService.validateEmail(email);
        if (isExistsMail) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || authService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(authService.refreshToken(refreshToken))
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || authService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(newAccessToken)
                .build());
    }

    // logout ลบ cookie refresh token
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.ok().build();
    }
}

