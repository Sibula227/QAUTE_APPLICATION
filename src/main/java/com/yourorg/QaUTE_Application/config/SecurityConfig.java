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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF (tùy chọn, để đơn giản như yêu cầu)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Cấu hình quyền truy cập (Authorize)
                .authorizeHttpRequests(auth -> auth
                        // -- Các đường dẫn công khai (Không cần đăng nhập) --
                        .requestMatchers(
                                "/", // Trang chủ (nếu muốn)
                                "/login", "/register", // Trang giao diện Login/Register
                                "/error", // Error Page (Tránh lặp redirect khi lỗi)
                                "/api/auth/**", // API Đăng nhập/Đăng ký
                                "/css/**", "/js/**", "/images/**", "/webjars/**", // File tĩnh
                                "/swagger-ui/**", "/v3/api-docs/**", // Swagger Docs
                                "/ws/**" // WebSocket Endpoint
                        ).permitAll()

                        // -- Phân quyền cụ thể (Ví dụ) --
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/counselor/**").hasAnyRole("COUNSELOR", "ADMIN")

                        // -- Tất cả các request còn lại PHẢI đăng nhập --
                        .anyRequest().authenticated())

                // 3. Form Login (Đăng nhập cơ bản - Sử dụng trang mặc định của Spring)
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true) // Redirect sau khi login thành công
                        .permitAll())

                // 4. Logout
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())

                // 5. Cấu hình Authentication Provider (Để check user/pass từ DB)
                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    // --- CÁC BEAN CẦN THIẾT CHO CONTROLLER ---

    // Bean này giúp mã hóa mật khẩu (Controller dùng để mã hóa khi đăng ký)
    // SỬ DỤNG TEXT THƯỜNG (KHÔNG MÃ HÓA) THEO YÊU CẦU CỦA USER "BỎ BẢO MẬT PHỨC
    // TẠP"
    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
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
