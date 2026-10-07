package com.crustercrew.userservice.configs;

import com.crustercrew.enums.UserRole;
import com.crustercrew.userservice.entity.User;
import com.crustercrew.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataUserInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUsers() {
        return args -> {
            if (userRepository.count() == 0) {
                // 1. Staff Requester
                userRepository.save(User.builder()
                        .email("requester@company.com")
                        .passwordHash(passwordEncoder.encode("password123"))
                        .fullName("Staff Pengadaan")
                        .role(UserRole.REQUESTER)
                        .departmentId(1L)
                        .isActive(true)
                        .build());

                // 2. Manager Approver
                userRepository.save(User.builder()
                        .email("manager@company.com")
                        .passwordHash(passwordEncoder.encode("password123"))
                        .fullName("Manager Procurement")
                        .role(UserRole.MANAGER)
                        .departmentId(1L)
                        .isActive(true)
                        .build());

                // 3. Vendor User
                userRepository.save(User.builder()
                        .email("sales@dell.co.id")
                        .passwordHash(passwordEncoder.encode("password123"))
                        .fullName("PT Dell Indonesia")
                        .role(UserRole.VENDOR)
                        .isActive(true)
                        .build());

                // 4. Admin
                userRepository.save(User.builder()
                        .email("admin@company.com")
                        .passwordHash(passwordEncoder.encode("password123"))
                        .fullName("System Administrator")
                        .role(UserRole.ADMIN)
                        .isActive(true)
                        .build());

                System.out.println(">>> Sample Users (Requester, Manager, Vendor, Admin) berhasil di-seed ke DB!");
            }
        };
    }
}