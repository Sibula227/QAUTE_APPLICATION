package com.yourorg.QaUTE_Application.controller;

import com.yourorg.QaUTE_Application.dto.auth.RegisterRequest;
import com.yourorg.QaUTE_Application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok("Đăng ký thành công!");
    }
}