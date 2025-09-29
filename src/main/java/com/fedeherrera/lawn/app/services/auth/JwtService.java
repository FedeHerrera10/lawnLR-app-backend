package com.fedeherrera.lawn.app.services.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

import static com.fedeherrera.lawn.app.security.TokenJwtConfig.*;


@Service
public class JwtService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    public String generateToken(String username) {
        try {
         List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));   
            

    Map<String, Object> claims = new HashMap<>();
    claims.put("username", username);
    claims.put("authorities", objectMapper.writeValueAsString(roles)); // si querés incluir roles
        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();
        return token;
        } catch (Exception e) {
            return null;
        }
    }

}
