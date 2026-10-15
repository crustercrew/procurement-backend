package com.crustercrew.authservice.services;

import com.crustercrew.authservice.configs.JwtProperties;
import com.crustercrew.authservice.dto.response.AuthResponse;
import com.crustercrew.authservice.dto.response.UserResponse;
import com.crustercrew.authservice.entity.RefreshToken;
import com.crustercrew.authservice.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse issue(UserResponse user) {
        Instant now = Instant.now();
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("http://localhost:8081")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofMillis(jwtProperties.getAccessExpiration())))
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .claim("roles", List.of("ROLE_" + user.getRole()))
                .claim("scope", "read write");
        if (user.getDepartmentId() != null) {
            claimsBuilder.claim("departmentId", user.getDepartmentId());
        }
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(user.getId())
                .email(user.getEmail())
                .expiresAt(LocalDateTime.now().plus(Duration.ofMillis(jwtProperties.getRefreshExpiration())))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .departmentId(user.getDepartmentId())
                .build();
    }
}
