package com.crustercrew.userservice.entity;

import com.crustercrew.userservice.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // REQUESTER, MANAGER, VENDOR, ADMIN
    private Long departmentId;
    private String phone;
    private String address;
    // Khusus untuk user dengan role VENDOR
    private String companyName;
    private String taxId;
    @Builder.Default
    private Boolean isActive = true;
}
