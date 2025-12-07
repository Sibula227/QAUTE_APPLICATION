package com.yourorg.QaUTE_Application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") // Người nhận thông báo
    private User user;

    private String type; // Ví dụ: "APPOINTMENT_APPROVED", "NEW_MESSAGE"
    private String payload; // Nội dung thông báo
    
    private boolean seen; // Đã xem chưa

    @CreationTimestamp
    private LocalDateTime createdAt;
}
