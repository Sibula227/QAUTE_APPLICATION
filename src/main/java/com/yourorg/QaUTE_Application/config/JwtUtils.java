

package com.yourorg.QaUTE_Application.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    // 1. Secret Key: Chuỗi bí mật dùng để ký tên (nên để dài và phức tạp)
    // Trong thực tế nên để trong file application.properties
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // 2. Thời gian hết hạn (ví dụ: 7 ngày)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; 

    // --- TẠO TOKEN ---
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // --- LẤY USERNAME TỪ TOKEN ---
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // --- KIỂM TRA TOKEN CÓ HỢP LỆ KHÔNG ---
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Token không đúng định dạng");
        } catch (ExpiredJwtException e) {
            System.err.println("Token đã hết hạn");
        } catch (UnsupportedJwtException e) {
            System.err.println("Token không được hỗ trợ");
        } catch (IllegalArgumentException e) {
            System.err.println("Token rỗng");
        }
        return false;
    }

    // --- CÁC HÀM PHỤ TRỢ ---
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}