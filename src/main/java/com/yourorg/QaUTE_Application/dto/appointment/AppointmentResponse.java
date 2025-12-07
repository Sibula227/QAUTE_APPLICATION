package com.yourorg.QaUTE_Application.dto.appointment;

import com.yourorg.QaUTE_Application.entity.AppointmentStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponse {
    private Long id;
    
    // Thay vì trả về cả object User, ta chỉ trả về tên cho nhẹ
    private Long studentId;
    private String studentName;
    private String studentAvatar;

    private Long counselorId;
    private String counselorName;
    
    private String topicName;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    private AppointmentStatus status;
}