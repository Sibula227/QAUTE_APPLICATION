package com.yourorg.QaUTE_Application.controller;

import com.yourorg.QaUTE_Application.config.JwtUtils;
import com.yourorg.QaUTE_Application.dto.auth.AuthResponse;
import com.yourorg.QaUTE_Application.dto.auth.LoginRequest;
import com.yourorg.QaUTE_Application.dto.auth.RegisterRequest;
import com.yourorg.QaUTE_Application.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    // Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        // 1. Spring Security kiểm tra username/password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Nếu đúng, tạo Token
        String token = jwtUtils.generateToken(request.getUsername());

        // 3. Tạo Cookie (để Thymeleaf dùng)
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(cookie);

        // 4. Trả về JSON (để AJAX/Mobile dùng)
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(request.getUsername())
                .build());
    }
}