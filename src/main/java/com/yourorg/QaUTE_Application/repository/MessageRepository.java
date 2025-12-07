package com.yourorg.QaUTE_Application.repository;

import com.yourorg.QaUTE_Application.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Lấy toàn bộ tin nhắn của một cuộc hẹn
    // OrderBySentAtAsc: Sắp xếp tin nhắn cũ trước, mới sau (từ trên xuống dưới)
    List<Message> findByAppointmentIdOrderBySentAtAsc(Long appointmentId);
}
