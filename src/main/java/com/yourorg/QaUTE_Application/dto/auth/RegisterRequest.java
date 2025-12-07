package com.yourorg.QaUTE_Application.dto.auth;

import com.yourorg.QaUTE_Application.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, message = "Mật khẩu phải từ 6 ký tự trở lên")
    private String password;

    private String fullName;
    
    // Mặc định là STUDENT, nếu muốn đăng ký Counselor thì truyền vào
    private Role role; 
}