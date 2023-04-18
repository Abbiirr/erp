package com.brainstation23.erp.util;

import com.brainstation23.erp.model.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenUtil {
    @Value("my_secret_key")
    private String secret = "my_secret_key";

    private static final String JWT_SECRET_KEY = "my_secret_key";

    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 86400000);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY.getBytes()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

    public static boolean checkIfAdmin(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role").equals("ADMIN");
    }

    public static String extractRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("role");
    }
}

