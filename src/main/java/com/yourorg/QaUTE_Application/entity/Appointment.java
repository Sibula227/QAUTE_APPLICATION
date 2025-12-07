package com.yourorg.QaUTE_Application.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id") // Link tới User (Role STUDENT)
    private User student;

    @ManyToOne
    @JoinColumn(name = "counselor_id") // Link tới User (Role COUNSELOR)
    private User counselor;
    
    // Nếu bạn muốn link cuộc hẹn tới 1 chủ đề cụ thể
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;
}
