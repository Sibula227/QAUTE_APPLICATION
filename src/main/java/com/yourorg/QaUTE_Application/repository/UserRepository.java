package com.yourorg.QaUTE_Application.repository;
// Quản lý đăng nhập/đăng ký

//Cần các hàm tìm kiếm theo username/email để xác thực.

import com.yourorg.QaUTE_Application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm user để đăng nhập (Spring Security sẽ dùng cái này)
    Optional<User> findByUsername(String username);

    // Kiểm tra trùng lặp khi đăng ký
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}