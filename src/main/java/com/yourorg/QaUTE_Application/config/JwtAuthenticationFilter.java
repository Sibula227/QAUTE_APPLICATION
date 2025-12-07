package com.yourorg.QaUTE_Application.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;
        String username = null;

        // 1. Lấy Token từ HEADER (Dùng cho Postman, Swagger, Mobile App)
        // Định dạng: "Authorization: Bearer <token>"
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2. Nếu Header không có, tìm trong COOKIE (Dùng cho Web Thymeleaf)
        // Vì trình duyệt tự động gửi Cookie, ta cần tìm cookie tên là "JWT_TOKEN"
        if (token == null && request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 3. Nếu tìm thấy Token, tiến hành xác thực
        if (token != null) {
            // Lấy username từ token
            username = jwtUtils.extractUsername(token);

            // Kiểm tra xem user này đã được xác thực trong Session chưa
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Tải thông tin user từ Database lên
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Kiểm tra token có còn hạn và đúng chữ ký không
                if (jwtUtils.validateToken(token)) {
                    // Tạo object xác thực
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // LƯU VÀO SECURITY CONTEXT -> Đánh dấu là "Đã Đăng Nhập"
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // 4. Cho phép request đi tiếp đến các Controller
        filterChain.doFilter(request, response);
    }
}