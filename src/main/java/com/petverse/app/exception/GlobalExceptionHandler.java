package com.petverse.app.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", badRequest.getReasonPhrase());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(badRequest).body(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", unauthorized.getReasonPhrase());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(unauthorized).body(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", unauthorized.getReasonPhrase());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(unauthorized).body(error);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Map<String, String>> handleMalformedJwtException(MalformedJwtException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", unauthorized.getReasonPhrase());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(unauthorized).body(error);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<Map<String, String>> handleSignatureException(SignatureException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", unauthorized.getReasonPhrase());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(unauthorized).body(error);
    }

    // เพิ่ม handler อื่นได้ เช่น MethodArgumentNotValidException เป็นต้น
}

