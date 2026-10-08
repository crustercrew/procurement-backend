package com.crustercrew.catalogservice.services;

import com.crustercrew.catalogservice.dto.request.CreateUserRequest;
import com.crustercrew.catalogservice.dto.request.CreateVendorRequest;
import com.crustercrew.catalogservice.dto.request.UpdateVendorRequest;
import com.crustercrew.catalogservice.dto.response.UserResponse;
import com.crustercrew.catalogservice.dto.response.VendorResponse;
import com.crustercrew.catalogservice.entity.Vendor;
import com.crustercrew.catalogservice.integrations.UserServiceAPI;
import com.crustercrew.catalogservice.repositories.VendorRepository;
import com.crustercrew.dto.APIResponse;
import com.crustercrew.exception.baseException.BusinessValidationException;
import com.crustercrew.exception.baseException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final UserServiceAPI userServiceAPI;

    @Transactional(readOnly = true)
    public Page<VendorResponse> getAllVendors(Pageable pageable) {
        return vendorRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public VendorResponse getVendorById(Long id) {
        Vendor vendor = findOrThrow(id);
        VendorResponse response = toResponse(vendor);

        if (vendor.getUserId() != null) {
            try {
                APIResponse<UserResponse> userResponse = userServiceAPI.getUserById(vendor.getUserId());
                if (userResponse != null && userResponse.data() != null) {
                    response.setUser(userResponse.data());
                }
            } catch (Exception e) {
                log.warn("Gagal mengambil data user untuk vendor id {}: {}", id, e.getMessage());
            }
        }

        return response;
    }

    @Transactional(readOnly = true)
    public VendorResponse getVendorByUserId(Long userId) {
        Vendor vendor = vendorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor dengan user ID '" + userId + "' tidak ditemukan"));
        VendorResponse response = toResponse(vendor);

        try {
            APIResponse<UserResponse> userResponse = userServiceAPI.getUserById(userId);
            if (userResponse != null && userResponse.data() != null) {
                response.setUser(userResponse.data());
            }
        } catch (Exception e) {
            log.warn("Gagal mengambil data user untuk userId {}: {}", userId, e.getMessage());
        }

        return response;
    }

    @Transactional
    public VendorResponse createVendor(CreateVendorRequest request) {
        // 1. Cek apakah NPWP sudah terdaftar
        if (vendorRepository.findByTaxIdNpwp(request.getTaxIdNpwp()) != null) {
            throw new BusinessValidationException("NPWP '" + request.getTaxIdNpwp() + "' sudah terdaftar");
        }

        Long assignedUserId = null;
        UserResponse createdUserResponse = null;

        // 2. Opsi A: Sekaligus buat user baru jika data user disediakan
        if (request.getUser() != null) {
            String userEmail = StringUtils.hasText(request.getUser().getEmail())
                    ? request.getUser().getEmail().trim()
                    : request.getContactEmail().trim();

            String fullName = StringUtils.hasText(request.getUser().getFullName())
                    ? request.getUser().getFullName().trim()
                    : request.getCompanyName().trim();

            CreateUserRequest userReq = CreateUserRequest.builder()
                    .email(userEmail)
                    .fullName(fullName)
                    .password(request.getUser().getPassword())
                    .role("VENDOR")
                    .departmentId(null)
                    .build();

            try {
                APIResponse<UserResponse> userApiResponse = userServiceAPI.createUser(userReq);
                if (userApiResponse != null && userApiResponse.data() != null) {
                    createdUserResponse = userApiResponse.data();
                    assignedUserId = createdUserResponse.getId();
                } else {
                    throw new BusinessValidationException("Gagal membuat akun user untuk vendor: respons user-service kosong");
                }
            } catch (BusinessValidationException e) {
                throw e;
            } catch (Exception e) {
                throw new BusinessValidationException("Gagal menghubungi user-service untuk membuat user: " + e.getMessage());
            }
        }
        // Opsi B: Mengaitkan dengan existing userId jika diberikan
        else if (request.getUserId() != null) {
            if (vendorRepository.findByUserId(request.getUserId()).isPresent()) {
                throw new BusinessValidationException("User ID '" + request.getUserId() + "' sudah terdaftar sebagai vendor lain");
            }
            assignedUserId = request.getUserId();
        }
        // Opsi C: Tanpa user (assignedUserId tetap null)

        Vendor vendor = Vendor.builder()
                .userId(assignedUserId)
                .companyName(request.getCompanyName())
                .taxIdNpwp(request.getTaxIdNpwp())
                .contactEmail(request.getContactEmail())
                .isActive(true)
                .build();

        Vendor saved = vendorRepository.save(vendor);
        VendorResponse response = toResponse(saved);
        if (createdUserResponse != null) {
            response.setUser(createdUserResponse);
        }

        return response;
    }

    @Transactional
    public VendorResponse updateVendor(Long id, UpdateVendorRequest request) {
        Vendor vendor = findOrThrow(id);

        if (StringUtils.hasText(request.getCompanyName())) {
            vendor.setCompanyName(request.getCompanyName());
        }
        if (StringUtils.hasText(request.getTaxIdNpwp())) {
            vendor.setTaxIdNpwp(request.getTaxIdNpwp());
        }
        if (StringUtils.hasText(request.getContactEmail())) {
            vendor.setContactEmail(request.getContactEmail());
        }

        return toResponse(vendorRepository.save(vendor));
    }

    @Transactional
    public VendorResponse deactivateVendor(Long id) {
        Vendor vendor = findOrThrow(id);
        vendor.setIsActive(false);
        return toResponse(vendorRepository.save(vendor));
    }

    // â”€â”€â”€ Private Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private Vendor findOrThrow(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor dengan ID '" + id + "' tidak ditemukan"));
    }

    private VendorResponse toResponse(Vendor vendor) {
        return VendorResponse.builder()
                .id(vendor.getId())
                .userId(vendor.getUserId())
                .companyName(vendor.getCompanyName())
                .taxIdNpwp(vendor.getTaxIdNpwp())
                .contactEmail(vendor.getContactEmail())
                .isActive(vendor.getIsActive())
                .build();
    }
}