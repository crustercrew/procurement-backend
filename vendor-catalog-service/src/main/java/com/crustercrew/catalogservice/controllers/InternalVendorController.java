package com.crustercrew.catalogservice.controllers;

import com.crustercrew.catalogservice.dto.response.VendorCatalogItemResponse;
import com.crustercrew.catalogservice.dto.response.VendorResponse;
import com.crustercrew.catalogservice.services.CatalogService;
import com.crustercrew.catalogservice.services.VendorService;
import com.crustercrew.dto.APIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal endpoints untuk konsumsi antar-service lewat Feign. Tidak diekspos lewat
 * API Gateway (tidak ada route ke /internal/**).
 */
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalVendorController {

    private final VendorService vendorService;
    private final CatalogService catalogService;

    @GetMapping("/vendors/{id}")
    public ResponseEntity<APIResponse<VendorResponse>> getVendorById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan data vendor", vendorService.getVendorInternalById(id))
        );
    }

    @GetMapping("/vendors/by-user/{userId}")
    public ResponseEntity<APIResponse<VendorResponse>> getVendorByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan data vendor", vendorService.getVendorInternalByUserId(userId))
        );
    }

    @GetMapping("/catalog-items/{id}")
    public ResponseEntity<APIResponse<VendorCatalogItemResponse>> getCatalogItemById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan catalog item", catalogService.getCatalogItemById(id))
        );
    }
}
