package com.crustercrew.catalogservice.controllers;

import com.crustercrew.catalogservice.dto.request.CreateCatalogItemRequest;
import com.crustercrew.catalogservice.dto.request.UpdateCatalogItemRequest;
import com.crustercrew.catalogservice.dto.response.VendorCatalogItemResponse;
import com.crustercrew.catalogservice.services.CatalogService;
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
@RequiredArgsConstructor
@Tag(name = "Catalog Controller", description = "Manajemen katalog barang vendor")
public class CatalogController {

    private final CatalogService catalogService;

    /**
     * Mendukung 2 path:
     * - /api/v1/catalog (canonical path sesuai system design)
     * - /catalog-items   (legacy path yang dipanggil oleh CatalogFeignClient di purchase-requisition-service)
     */
    @GetMapping({"/api/v1/catalog", "/catalog-items"})
    @Operation(summary = "List catalog items", description = "Filter: ?keyword=&vendorId=&category= + pageable")
    public ResponseEntity<APIResponse<Page<VendorCatalogItemResponse>>> getCatalogItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan katalog",
                        catalogService.getCatalogItems(keyword, vendorId, category, pageable))
        );
    }

    @GetMapping({"/api/v1/catalog/{id}", "/catalog-items/{id}"})
    @Operation(summary = "Detail catalog item by ID")
    public ResponseEntity<APIResponse<VendorCatalogItemResponse>> getCatalogItemById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan catalog item", catalogService.getCatalogItemById(id))
        );
    }

    @PostMapping({"/api/v1/catalog", "/catalog-items"})
    @Operation(summary = "Tambah catalog item baru")
    public ResponseEntity<APIResponse<VendorCatalogItemResponse>> createCatalogItem(
            @Valid @RequestBody CreateCatalogItemRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                APIResponse.success(201, "Catalog item berhasil dibuat", catalogService.createCatalogItem(request))
        );
    }

    @PutMapping({"/api/v1/catalog/{id}", "/catalog-items/{id}"})
    @Operation(summary = "Update catalog item")
    public ResponseEntity<APIResponse<VendorCatalogItemResponse>> updateCatalogItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCatalogItemRequest request
    ) {
        return ResponseEntity.ok(
                APIResponse.success("Catalog item berhasil diperbarui", catalogService.updateCatalogItem(id, request))
        );
    }
}