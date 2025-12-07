package com.yourorg.QaUTE_Application.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    // Dữ liệu gửi lên
    private Long appointmentId; // Chat trong cuộc hẹn nào
    private String content;

    // Dữ liệu trả về (Server điền thêm)
    private Long senderId;
    private String senderName;
    private String senderAvatar;
    private LocalDateTime sentAt;
    
    // Cờ để giao diện biết tin nhắn này của "tôi" hay "người khác"
    private boolean isMyMessage; 
}