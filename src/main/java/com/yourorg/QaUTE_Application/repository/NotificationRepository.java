package com.yourorg.QaUTE_Application.repository;

import com.yourorg.QaUTE_Application.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Lấy danh sách thông báo của một user, cái mới nhất lên đầu
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Lấy các thông báo chưa đọc (để hiện số đỏ trên icon chuông)
    List<Notification> findByUserIdAndSeenFalse(Long userId);
}