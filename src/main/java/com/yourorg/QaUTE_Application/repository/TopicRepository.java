package com.yourorg.QaUTE_Application.repository;

import com.yourorg.QaUTE_Application.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    // Có thể thêm hàm tìm theo tên nếu cần
    boolean existsByName(String name);
}
