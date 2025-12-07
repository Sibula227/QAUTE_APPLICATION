package com.yourorg.QaUTE_Application.dto.appointment;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private Long counselorId; // Muốn gặp ai
    private Long topicId;     // Chủ đề gì
    
    // Định dạng ngày giờ: YYYY-MM-DDTHH:mm:ss (ISO 8601)
    // Ví dụ: 2023-12-25T09:30:00
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
