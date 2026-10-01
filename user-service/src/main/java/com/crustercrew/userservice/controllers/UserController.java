package com.crustercrew.userservice.controllers;

import com.crustercrew.userservice.entity.User;
import com.crustercrew.userservice.entity.enums.UserRole;
import com.crustercrew.userservice.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Manajemen Pengguna (User Management)")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    @Operation(summary = "Get all users", description = "Mengambil semua daftar user yang terdaftar")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("content", users);
        response.put("_embedded", Map.of("users", users));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Mengambil detail user berdasarkan ID (digunakan juga oleh Feign Client Requisition Service)")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/findByRole")
    @Operation(summary = "Search users by role", description = "Mencari pengguna berdasarkan role (REQUESTER, MANAGER, VENDOR, ADMIN)")
    public ResponseEntity<?> findByRole(@RequestParam("role") String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            List<User> users = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == userRole)
                    .toList();
            Map<String, Object> response = new HashMap<>();
            response.put("content", users);
            response.put("_embedded", Map.of("users", users));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Role tidak valid: " + role));
        }
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Menambahkan pengguna baru ke dalam sistem")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
