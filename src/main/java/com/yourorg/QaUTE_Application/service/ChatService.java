package com.yourorg.QaUTE_Application.service;

import com.yourorg.QaUTE_Application.dto.chat.MessageDto;
import com.yourorg.QaUTE_Application.entity.Appointment;
import com.yourorg.QaUTE_Application.entity.Message;
import com.yourorg.QaUTE_Application.entity.User;
import com.yourorg.QaUTE_Application.repository.AppointmentRepository;
import com.yourorg.QaUTE_Application.repository.MessageRepository;
import com.yourorg.QaUTE_Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    // Lưu tin nhắn vào DB
    @Transactional
    public MessageDto saveMessage(MessageDto messageDto) {
        Appointment appointment = appointmentRepository.findById(messageDto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Cuộc hẹn không tồn tại"));

        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Tạo Entity
        Message message = new Message();
        message.setAppointment(appointment);
        message.setSender(sender);
        message.setContent(messageDto.getContent());
        
        // Lưu
        Message savedMsg = messageRepository.save(message);

        // Trả về DTO để WebSocket gửi lại cho client
        return MessageDto.builder()
                .appointmentId(appointment.getId())
                .content(savedMsg.getContent())
                .senderId(sender.getId())
                .senderName(sender.getFullName())
                .senderAvatar(sender.getAvatarUrl())
                .sentAt(savedMsg.getSentAt())
                .build();
    }

    // Lấy lịch sử chat
    public List<MessageDto> getChatHistory(Long appointmentId, Long currentUserId) {
        List<Message> messages = messageRepository.findByAppointmentIdOrderBySentAtAsc(appointmentId);

        return messages.stream().map(msg -> MessageDto.builder()
                .appointmentId(msg.getAppointment().getId())
                .content(msg.getContent())
                .senderId(msg.getSender().getId())
                .senderName(msg.getSender().getFullName())
                .senderAvatar(msg.getSender().getAvatarUrl())
                .sentAt(msg.getSentAt())
                .isMyMessage(msg.getSender().getId().equals(currentUserId)) // Đánh dấu tin chính chủ
                .build()
        ).collect(Collectors.toList());
    }
}
