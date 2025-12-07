package com.yourorg.QaUTE_Application.repository;

import com.yourorg.QaUTE_Application.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    // Tìm hồ sơ sinh viên dựa vào User ID
    Optional<StudentProfile> findByUserId(Long userId);
    
    // Tìm theo mã sinh viên
    Optional<StudentProfile> findByStudentId(String studentId);
}
