package com.yourorg.QaUTE_Application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String studentId; // Mã số sinh viên
    private String faculty;   // Khoa
    private String schoolYear; // Khóa (Ví dụ: K19)
    
    @Column(columnDefinition = "TEXT")
    private String bio;
}