package com.crustercrew.authservice.controllers;

import com.crustercrew.authservice.dto.request.LoginRequest;
import com.crustercrew.authservice.dto.request.RefreshTokenRequest;
import com.crustercrew.authservice.dto.response.AuthResponse;
import com.crustercrew.authservice.services.AuthService;
import com.crustercrew.dto.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/v1/auth", "/api/auth"})
@RequiredArgsConstructor
public class AuthControllers {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(APIResponse.success("Login berhasil", authService.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(APIResponse.success("Token berhasil diperbarui", authService.refresh(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(APIResponse.success("Logout berhasil", null));
    }
}
