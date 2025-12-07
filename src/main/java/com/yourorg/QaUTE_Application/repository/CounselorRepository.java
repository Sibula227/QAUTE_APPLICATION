package com.yourorg.QaUTE_Application.repository;

import com.yourorg.QaUTE_Application.entity.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    // Tìm hồ sơ tư vấn viên dựa vào User ID
    Optional<Counselor> findByUserId(Long userId);
}