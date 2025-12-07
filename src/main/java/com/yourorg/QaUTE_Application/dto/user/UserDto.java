package com.yourorg.QaUTE_Application.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String role;
    
    // Thông tin thêm (nếu là sinh viên)
    private String studentId;
    private String faculty;
    
    // Thông tin thêm (nếu là tư vấn viên)
    private String department;
}