package com.crustercrew.userservice.controllers;

import com.crustercrew.dto.APIResponse;
import com.crustercrew.userservice.dto.request.CreateUserRequest;
import com.crustercrew.userservice.dto.request.UpdateUserRequest;
import com.crustercrew.userservice.dto.response.UserResponse;
import com.crustercrew.userservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/users", "/api/users"})
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Manajemen Pengguna (User Management)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Mengambil semua daftar user dengan pagination")
    public ResponseEntity<APIResponse<Page<UserResponse>>> getAllUsers(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                APIResponse.success("Success get all users", userService.getUsers(pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Mengambil detail user berdasarkan ID")
    public ResponseEntity<APIResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Success get user", userService.getUser(id))
        );
    }

    @GetMapping("/search/findByRole")
    @Operation(summary = "Search users by role", description = "Mencari pengguna berdasarkan role")
    public ResponseEntity<APIResponse<List<UserResponse>>> findByRole(@RequestParam("role") String role) {
        return ResponseEntity.ok(
                APIResponse.success("Success get users by role", userService.findByRole(role))
        );
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Menambahkan pengguna baru ke dalam sistem")
    public ResponseEntity<APIResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.success(201, "User berhasil dibuat", userService.createUser(request))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Memperbarui data pengguna berdasarkan ID")
    public ResponseEntity<APIResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(
                APIResponse.success("User berhasil diupdate", userService.updateUser(id, request))
        );
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Menonaktifkan status pengguna")
    public ResponseEntity<APIResponse<UserResponse>> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("User berhasil dinonaktifkan", userService.deactivateUser(id))
        );
    }
}