package com.crustercrew.project.procurementjavabe.service;

import com.crustercrew.project.procurementjavabe.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    public String generateToken() {
        String now = Date.from(Instant.now()).toString();
        String expiration = Date.from(Instant.now().plusSeconds(jwtProperties.getExpiration())).toString();
        return "";
    }
}
