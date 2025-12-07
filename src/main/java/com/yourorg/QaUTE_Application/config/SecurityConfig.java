package com.yourorg.QaUTE_Application.config;

import com.yourorg.QaUTE_Application.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Tắt CSRF (Do dùng JWT và cơ chế Stateless)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. Cấu hình quyền truy cập (Authorize)
            .authorizeHttpRequests(auth -> auth
                // -- Các đường dẫn công khai (Không cần đăng nhập) --
                .requestMatchers(
                        "/",                    // Trang chủ (nếu muốn)
                        "/login", "/register",  // Trang giao diện Login/Register
                        "/api/auth/**",         // API Đăng nhập/Đăng ký
                        "/css/**", "/js/**", "/images/**", "/webjars/**", // File tĩnh
                        "/swagger-ui/**", "/v3/api-docs/**", // Swagger Docs
                        "/ws/**"                // WebSocket Endpoint
                ).permitAll()
                
                // -- Phân quyền cụ thể (Ví dụ) --
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/counselor/**").hasAnyRole("COUNSELOR", "ADMIN")
                
                // -- Tất cả các request còn lại PHẢI đăng nhập --
                .anyRequest().authenticated()
            )
            
            // 3. Quản lý Session: STATELESS (Quan trọng khi dùng JWT)
            // Server không lưu session, mỗi request phải kèm Token
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. Cấu hình Authentication Provider (Để check user/pass từ DB)
            .authenticationProvider(authenticationProvider())
            
            // 5. Thêm Filter JWT vào trước Filter xác thực mặc định của Spring
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- CÁC BEAN CẦN THIẾT CHO CONTROLLER ---

    // Bean này giúp mã hóa mật khẩu (Controller dùng để mã hóa khi đăng ký)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean này giúp xác thực user (Controller dùng để check login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean này kết nối Logic tìm user (UserDetailsServiceImpl) với PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
