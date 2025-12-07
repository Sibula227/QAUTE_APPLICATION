package com.yourorg.QaUTE_Application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Ví dụ: Học vụ, Tâm lý, Hướng nghiệp
    
    @Column(columnDefinition = "TEXT")
    private String description;
}