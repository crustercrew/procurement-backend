package com.crustercrew.catalogservice.controllers;

import com.crustercrew.catalogservice.dto.CatalogItemRequest;
import com.crustercrew.catalogservice.entity.CatalogItem;
import com.crustercrew.catalogservice.entity.Category;
import com.crustercrew.catalogservice.repositories.CatalogItemRepository;
import com.crustercrew.catalogservice.repositories.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog-items")
@RequiredArgsConstructor
@Tag(name = "Catalog Item Controller", description = "Manajemen Item Katalog Barang Pengadaan")
public class CatalogItemController {

    private final CatalogItemRepository catalogItemRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    @Operation(summary = "Get all catalog items", description = "Mengambil semua daftar item katalog")
    public ResponseEntity<?> getAllCatalogItems() {
        List<CatalogItem> items = catalogItemRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("content", items);
        response.put("_embedded", Map.of("catalogItems", items));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get catalog item by ID", description = "Mengambil detail item katalog berdasarkan ID (digunakan juga oleh Feign Client Requisition Service)")
    public ResponseEntity<CatalogItem> getCatalogItemById(@PathVariable Long id) {
        return catalogItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create catalog item", description = "Menambahkan item katalog baru")
    public ResponseEntity<CatalogItem> createCatalogItem(@RequestBody CatalogItemRequest request) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }

        BigDecimal price = request.getUnitPrice() != null ? request.getUnitPrice() : request.getPrice();
        String uom = request.getUnitOfMeasure() != null ? request.getUnitOfMeasure() : (request.getUnit() != null ? request.getUnit() : "UNIT");
        String sku = request.getSku() != null ? request.getSku() : ("SKU-" + System.currentTimeMillis());

        CatalogItem item = CatalogItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .sku(sku)
                .unitPrice(price != null ? price : BigDecimal.ZERO)
                .unitOfMeasure(uom)
                .vendorId(request.getVendorId() != null ? request.getVendorId() : 1L)
                .category(category)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        CatalogItem saved = catalogItemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update catalog item", description = "Memperbarui item katalog")
    public ResponseEntity<CatalogItem> updateCatalogItem(@PathVariable Long id, @RequestBody CatalogItemRequest request) {
        return catalogItemRepository.findById(id).map(existing -> {
            if (request.getName() != null) existing.setName(request.getName());
            if (request.getDescription() != null) existing.setDescription(request.getDescription());
            if (request.getUnitPrice() != null) existing.setUnitPrice(request.getUnitPrice());
            else if (request.getPrice() != null) existing.setUnitPrice(request.getPrice());
            if (request.getUnitOfMeasure() != null) existing.setUnitOfMeasure(request.getUnitOfMeasure());
            else if (request.getUnit() != null) existing.setUnitOfMeasure(request.getUnit());
            if (request.getIsActive() != null) existing.setIsActive(request.getIsActive());
            if (request.getCategoryId() != null) {
                categoryRepository.findById(request.getCategoryId()).ifPresent(existing::setCategory);
            }
            return ResponseEntity.ok(catalogItemRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete catalog item", description = "Menghapus item katalog berdasarkan ID")
    public ResponseEntity<Void> deleteCatalogItem(@PathVariable Long id) {
        if (catalogItemRepository.existsById(id)) {
            catalogItemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
