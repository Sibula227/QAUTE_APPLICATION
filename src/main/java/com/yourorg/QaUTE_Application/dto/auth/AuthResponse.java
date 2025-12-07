package com.yourorg.QaUTE_Application.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;       // JWT Token
    private String username;
    private String role;
    private String fullName;
    private Long userId;
}