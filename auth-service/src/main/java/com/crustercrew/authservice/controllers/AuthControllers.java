package com.crustercrew.authservice.controllers;

import com.crustercrew.authservice.dto.request.LoginRequest;
import com.crustercrew.authservice.dto.response.AuthResponse;
import com.crustercrew.authservice.dto.response.UserResponse;
import com.crustercrew.authservice.integrations.UserClient;
import com.crustercrew.dto.APIResponse;
import com.crustercrew.exception.baseException.UnauthorizedAccessException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControllers {

    private final UserClient userClient;
    private final JwtEncoder jwtEncoder;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        APIResponse<UserResponse> userApiResponse;
        try {
            userApiResponse = userClient.verifyCredentials(request);
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Email atau password tidak sesuai");
        }
        if (userApiResponse == null || userApiResponse.data() == null) {
            throw new UnauthorizedAccessException("Gagal mengautentikasi pengguna");
        }
        UserResponse user = userApiResponse.data();
        Instant now = Instant.now();
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer("http://localhost:8081")
                .issuedAt(now)
                .expiresAt(now.plus(Duration.ofHours(1)))
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .claim("roles", List.of("ROLE_" + user.getRole()))
                .claim("scope", "read write");
        if (user.getDepartmentId() != null) {
            claimsBuilder.claim("departmentId", user.getDepartmentId());
        }
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
        String refreshToken = UUID.randomUUID().toString();
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .departmentId(user.getDepartmentId())
                .build();
        return ResponseEntity.ok(APIResponse.success("Login berhasil", authResponse));
    }
}
