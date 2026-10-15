package com.crustercrew.authservice.services;

import com.crustercrew.authservice.dto.request.LoginRequest;
import com.crustercrew.authservice.dto.request.RefreshTokenRequest;
import com.crustercrew.authservice.dto.response.AuthResponse;
import com.crustercrew.authservice.dto.response.UserResponse;
import com.crustercrew.authservice.entity.RefreshToken;
import com.crustercrew.authservice.integrations.UserClient;
import com.crustercrew.authservice.repositories.RefreshTokenRepository;
import com.crustercrew.dto.APIResponse;
import com.crustercrew.exception.baseException.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserClient userClient;
    private final AuthTokenService authTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        APIResponse<UserResponse> userApiResponse;
        try {
            userApiResponse = userClient.verifyCredentials(request);
        } catch (Exception e) {
            log.warn("Login failed for '{}': {}", request.getEmail(), e.toString());
            throw new UnauthorizedAccessException("Email atau password tidak sesuai");
        }
        if (userApiResponse == null || userApiResponse.data() == null) {
            throw new UnauthorizedAccessException("Gagal mengautentikasi pengguna");
        }
        return authTokenService.issue(userApiResponse.data());
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken stored = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedAccessException("Refresh token tidak valid"));
        if (Boolean.TRUE.equals(stored.getRevoked()) || stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedAccessException("Refresh token sudah kedaluwarsa atau dicabut");
        }

        UserResponse user;
        try {
            APIResponse<UserResponse> response = userClient.getUserById(stored.getUserId());
            user = response != null ? response.data() : null;
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Gagal memuat data pengguna");
        }
        if (user == null) {
            throw new UnauthorizedAccessException("Pengguna tidak ditemukan");
        }

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);
        return authTokenService.issue(user);
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByToken(request.getRefreshToken()).ifPresent(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
    }
}
