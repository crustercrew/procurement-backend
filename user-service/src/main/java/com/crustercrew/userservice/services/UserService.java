package com.crustercrew.userservice.services;

import com.crustercrew.dto.APIResponse;
import com.crustercrew.enums.UserRole;
import com.crustercrew.exception.baseException.BusinessValidationException;
import com.crustercrew.exception.baseException.ResourceNotFoundException;
import com.crustercrew.exception.baseException.UnauthorizedAccessException;
import com.crustercrew.userservice.dto.request.CreateUserRequest;
import com.crustercrew.userservice.dto.request.UpdateUserRequest;
import com.crustercrew.userservice.dto.response.DepartmentResponse;
import com.crustercrew.userservice.dto.response.UserResponse;
import com.crustercrew.userservice.entity.User;
import com.crustercrew.userservice.integrations.DepartmenServicesAPI;
import com.crustercrew.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DepartmenServicesAPI departmenServicesAPI;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new BusinessValidationException("Email '" + request.getEmail() + "' sudah terdaftar");
        }
        UserRole role;
        try {
            role = UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessValidationException(
                    "Role '" + request.getRole() + "' tidak valid. Pilihan: " + Arrays.toString(UserRole.values())
            );
        }
        // Validasi department via Feign Client jika departmentId diisi
        if (request.getDepartmentId() != null) {
            try {
                APIResponse<DepartmentResponse> dept = departmenServicesAPI.getDepartmentById(request.getDepartmentId());
                if (dept == null || dept.data() == null) {
                    throw new ResourceNotFoundException("Department dengan ID '" + request.getDepartmentId() + "' tidak ditemukan");
                }
            } catch (Exception e) {
                throw new ResourceNotFoundException("Department dengan ID '" + request.getDepartmentId() + "' tidak ditemukan");
            }
        }
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(role)
                .departmentId(request.getDepartmentId())
                .isActive(true)
                .build();
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID '" + id + "' tidak ditemukan"));
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findByRole(String roleStr) {
        UserRole role;
        try {
            role = UserRole.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessValidationException(
                    "Role '" + roleStr + "' tidak valid. Pilihan: " + Arrays.toString(UserRole.values())
            );
        }
        return userRepository.findByRole(role).stream().map(UserResponse::from).toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + id + " tidak ditemukan"));
        if (StringUtils.hasText(request.getFullName())) {
            user.setFullName(request.getFullName());
        }
        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }
        if (StringUtils.hasText(request.getRole())) {
            try {
                user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new BusinessValidationException("Role '" + request.getRole() + "' tidak valid");
            }
        }

        if (request.getDepartmentId() != null) {
            user.setDepartmentId(request.getDepartmentId());
        }
        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Transactional
    public UserResponse deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User dengan ID " + id + " tidak ditemukan"));
        user.setIsActive(false);
        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    public UserResponse verifyCredential(String email, String password){
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UnauthorizedAccessException("Email atau password salah!");
        }
        if(!Boolean.TRUE.equals(user.getIsActive())){
            throw new UnauthorizedAccessException("Akun sudah tidak aktif!");
        }
        return UserResponse.from(user);
    }
}