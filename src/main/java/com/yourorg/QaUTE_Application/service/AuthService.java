package com.yourorg.QaUTE_Application.service;

import com.yourorg.QaUTE_Application.dto.auth.RegisterRequest;
import com.yourorg.QaUTE_Application.entity.Role;
import com.yourorg.QaUTE_Application.entity.User;
import com.yourorg.QaUTE_Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(RegisterRequest request) {
        // 1. Kiểm tra trùng lặp
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Error: Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: Email đã được sử dụng!");
        }

        // 2. Tạo User Entity từ DTO
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        
        // 3. Mã hóa mật khẩu (Rất quan trọng)
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // 4. Set Role mặc định (Nếu request không gửi role thì mặc định là STUDENT)
        Role role = (request.getRole() != null) ? request.getRole() : Role.STUDENT;
        user.setRoles(new HashSet<>(Collections.singletonList(role)));

        // 5. Lưu xuống DB
        userRepository.save(user);
    }
}