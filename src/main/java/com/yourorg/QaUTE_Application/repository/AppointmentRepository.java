package com.yourorg.QaUTE_Application.repository;

import com.yourorg.QaUTE_Application.entity.Counselor;
import com.yourorg.QaUTE_Application.entity.Appointment;
import com.yourorg.QaUTE_Application.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 1. Lấy danh sách lịch hẹn của một Sinh viên
    List<Appointment> findByStudentId(Long studentId);

    // 2. Lấy danh sách lịch hẹn của một Tư vấn viên
    List<Appointment> findByCounselorId(Long counselorId);

    // 3. Tìm các lịch hẹn theo trạng thái (Ví dụ: Lấy danh sách PENDING để duyệt)
    List<Appointment> findByStatus(AppointmentStatus status);

    // 4. Tìm lịch hẹn của Counselor theo trạng thái cụ thể
    // Ví dụ: Tư vấn viên muốn xem các lịch đã "APPROVED" của mình
    List<Appointment> findByCounselorIdAndStatus(Long counselorId, AppointmentStatus status);
}