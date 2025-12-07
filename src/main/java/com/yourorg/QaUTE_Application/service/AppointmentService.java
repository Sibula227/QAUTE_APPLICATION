package com.yourorg.QaUTE_Application.service;

import com.yourorg.QaUTE_Application.dto.appointment.AppointmentRequest;
import com.yourorg.QaUTE_Application.dto.appointment.AppointmentResponse;
import com.yourorg.QaUTE_Application.entity.*;
import com.yourorg.QaUTE_Application.repository.AppointmentRepository;
import com.yourorg.QaUTE_Application.repository.TopicRepository;
import com.yourorg.QaUTE_Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;

    // --- TẠO LỊCH HẸN ---
    @Transactional
    public void createAppointment(Long studentId, AppointmentRequest request) {
        // 1. Kiểm tra logic ngày giờ
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Không thể đặt lịch trong quá khứ!");
        }
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new RuntimeException("Thời gian kết thúc không hợp lệ!");
        }

        // 2. Lấy thông tin Student, Counselor, Topic từ DB
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
        
        User counselor = userRepository.findById(request.getCounselorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tư vấn viên"));

        Topic topic = null;
        if(request.getTopicId() != null) {
            topic = topicRepository.findById(request.getTopicId()).orElse(null);
        }

        // 3. Tạo Entity
        Appointment appointment = Appointment.builder()
                .student(student)
                .counselor(counselor)
                .topic(topic)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AppointmentStatus.PENDING) // Mặc định là chờ duyệt
                .build();

        appointmentRepository.save(appointment);
    }

    // --- LẤY DANH SÁCH LỊCH HẸN (Cho SV hoặc GV) ---
    public List<AppointmentResponse> getAppointmentsByUserId(Long userId, boolean isStudent) {
        List<Appointment> list;
        if (isStudent) {
            list = appointmentRepository.findByStudentId(userId);
        } else {
            list = appointmentRepository.findByCounselorId(userId);
        }

        // Convert List<Entity> -> List<DTO>
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Hàm phụ: Chuyển đổi Entity sang DTO
    private AppointmentResponse mapToDTO(Appointment app) {
        return AppointmentResponse.builder()
                .id(app.getId())
                .studentId(app.getStudent().getId())
                .studentName(app.getStudent().getFullName())
                .studentAvatar(app.getStudent().getAvatarUrl())
                .counselorId(app.getCounselor().getId())
                .counselorName(app.getCounselor().getFullName())
                .topicName(app.getTopic() != null ? app.getTopic().getName() : "Khác")
                .startTime(app.getStartTime())
                .endTime(app.getEndTime())
                .status(app.getStatus())
                .build();
    }
    
    // --- DUYỆT / HỦY LỊCH ---
    @Transactional
    public void updateStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment app = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Lịch hẹn không tồn tại"));
        app.setStatus(newStatus);
        appointmentRepository.save(app);
    }
}