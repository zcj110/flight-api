package com.flight.api.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationInMs;

    // 生成 HMAC-SHA 类型的 SecretKey
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ✅ 生成 JWT Token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .subject(username)            // 替代 setSubject()
                .issuedAt(currentDate)         // 替代 setIssuedAt()
                .expiration(expireDate)        // 替代 setExpiration()
                .signWith(key())               // 自动识别算法类型（如 HS512）
                .compact();                    // 生成字符串形式的 token
    }

    // ✅ 从 Token 中提取用户名
    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key())  // 显式转换为 SecretKey
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // ✅ 验证 Token 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) key())
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            // Token 格式错误 / 过期 / 不支持 / 参数异常
            return false;
        }
    }
}