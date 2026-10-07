package com.crustercrew.authservice.configs;

import com.crustercrew.userservice.entity.User;
import com.crustercrew.userservice.entity.enums.UserRole;
import com.crustercrew.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initUsers() {
        return args -> {
            if (userRepository.count() == 0) {
                // 1. Staff Requester
                userRepository.save(User.builder()
                        .username("requester")
                        .password(passwordEncoder.encode("password123"))
                        .fullName("Staff Pengadaan")
                        .email("requester@company.com")
                        .role(UserRole.REQUESTER)
                        .departmentId(1L)
                        .build());
                // 2. Manager Approver
                userRepository.save(User.builder()
                        .username("manager")
                        .password(passwordEncoder.encode("password123"))
                        .fullName("Manager Procurement")
                        .email("manager@company.com")
                        .role(UserRole.MANAGER)
                        .departmentId(1L)
                        .build());
                // 3. Vendor User (Vendor sebagai Role)
                userRepository.save(User.builder()
                        .username("vendor_dell")
                        .password(passwordEncoder.encode("password123"))
                        .fullName("PT Dell Indonesia")
                        .email("sales@dell.co.id")
                        .role(UserRole.VENDOR)
                        .companyName("PT Dell Technologies Indonesia")
                        .taxId("01.234.567.8-999.000")
                        .address("Menara BCA Lt. 35, Jakarta Pusat")
                        .build());
                // 4. Admin
                userRepository.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("password123"))
                        .fullName("System Administrator")
                        .email("admin@company.com")
                        .role(UserRole.ADMIN)
                        .build());
                System.out.println(">>> Sample Users (Requester, Manager, Vendor, Admin) berhasil di-seed ke DB!");
            }
        };
    }
}
