package com.yourorg.QaUTE_Application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "counselors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counselor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String department; // Phòng ban (Phòng CTSV, Đào tạo...)
    private String officeHours; // Giờ trực
}