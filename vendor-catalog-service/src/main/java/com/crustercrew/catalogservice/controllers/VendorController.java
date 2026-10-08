package com.crustercrew.catalogservice.controllers;

import com.crustercrew.catalogservice.dto.request.CreateVendorRequest;
import com.crustercrew.catalogservice.dto.request.UpdateVendorRequest;
import com.crustercrew.catalogservice.dto.response.VendorResponse;
import com.crustercrew.catalogservice.services.VendorService;
import com.crustercrew.dto.APIResponse;
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

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendor Controller", description = "Manajemen data vendor")
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    @Operation(summary = "List semua vendor", description = "Menampilkan semua vendor dengan pagination")
    public ResponseEntity<APIResponse<Page<VendorResponse>>> getAllVendors(
            @PageableDefault(size = 20, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan data vendor", vendorService.getAllVendors(pageable))
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detail vendor by ID")
    public ResponseEntity<APIResponse<VendorResponse>> getVendorById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan data vendor", vendorService.getVendorById(id))
        );
    }

    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Cari vendor berdasarkan User ID", description = "Digunakan internal oleh PO dan Invoice Service")
    public ResponseEntity<APIResponse<VendorResponse>> getVendorByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan data vendor", vendorService.getVendorByUserId(userId))
        );
    }

    @PostMapping
    @Operation(summary = "Registrasi vendor baru")
    public ResponseEntity<APIResponse<VendorResponse>> createVendor(
            @Valid @RequestBody CreateVendorRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.success(201, "Vendor berhasil didaftarkan", vendorService.createVendor(request))
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update data vendor")
    public ResponseEntity<APIResponse<VendorResponse>> updateVendor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVendorRequest request
    ) {
        return ResponseEntity.ok(
                APIResponse.success("Vendor berhasil diperbarui", vendorService.updateVendor(id, request))
        );
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Nonaktifkan vendor")
    public ResponseEntity<APIResponse<VendorResponse>> deactivateVendor(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Vendor berhasil dinonaktifkan", vendorService.deactivateVendor(id))
        );
    }
}