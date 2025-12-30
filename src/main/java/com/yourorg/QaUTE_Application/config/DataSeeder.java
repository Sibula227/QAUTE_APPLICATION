package com.yourorg.QaUTE_Application.config;

import com.yourorg.QaUTE_Application.entity.Role;
import com.yourorg.QaUTE_Application.entity.User;
import com.yourorg.QaUTE_Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            System.out.println("----- SEEDING DATA -----");

            // Admin
            User admin = User.builder()
                    .username("admin")
                    .email("admin@hcmute.edu.vn")
                    .passwordHash("123456") // Plain text
                    .fullName("Administrator")
                    .roles(Set.of(Role.ADMIN))
                    .build();

            // Student
            User student = User.builder()
                    .username("student")
                    .email("student@hcmute.edu.vn")
                    .passwordHash("123456") // Plain text
                    .fullName("Nguyen Van Sinh Vien")
                    .roles(Set.of(Role.STUDENT))
                    .build();

            // Counselor
            User counselor = User.builder()
                    .username("counselor")
                    .email("counselor@hcmute.edu.vn")
                    .passwordHash("123456") // Plain text
                    .fullName("Tu Van Vien")
                    .roles(Set.of(Role.COUNSELOR))
                    .build();

            userRepository.saveAll(java.util.List.of(admin, student, counselor));
            System.out.println("----- DATA SEEDED SUCCESS (Pass: 123456) -----");
        }
    }
}
