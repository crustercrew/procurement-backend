package com.crustercrew.catalogservice.services;

import com.crustercrew.catalogservice.dto.request.CreateCatalogItemRequest;
import com.crustercrew.catalogservice.dto.request.UpdateCatalogItemRequest;
import com.crustercrew.catalogservice.dto.response.VendorCatalogItemResponse;
import com.crustercrew.catalogservice.entity.Vendor;
import com.crustercrew.catalogservice.entity.VendorCatalogItem;
import com.crustercrew.catalogservice.repositories.VendorCatalogItemRepository;
import com.crustercrew.catalogservice.repositories.VendorRepository;
import com.crustercrew.exception.baseException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final VendorCatalogItemRepository catalogItemRepository;
    private final VendorRepository vendorRepository;

    @Transactional(readOnly = true)
    public Page<VendorCatalogItemResponse> getCatalogItems(
            String keyword,
            Long vendorId,
            String category,
            Pageable pageable
    ) {
        Page<VendorCatalogItem> page;

        if (StringUtils.hasText(keyword)) {
            page = catalogItemRepository.searchByKeyword(keyword, pageable);
        } else if (vendorId != null) {
            page = catalogItemRepository.findByVendorIdAndIsActiveTrue(vendorId, pageable);
        } else if (StringUtils.hasText(category)) {
            page = catalogItemRepository.findByCategoryAndIsActiveTrue(category, pageable);
        } else {
            page = catalogItemRepository.findByIsActiveTrue(pageable);
        }

        return page.map(VendorCatalogItemResponse::from);
    }

    @Transactional(readOnly = true)
    public VendorCatalogItemResponse getCatalogItemById(Long id) {
        VendorCatalogItem item = catalogItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog item dengan ID '" + id + "' tidak ditemukan"));
        return VendorCatalogItemResponse.from(item);
    }

    @Transactional
    public VendorCatalogItemResponse createCatalogItem(CreateCatalogItemRequest request) {
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor dengan ID '" + request.getVendorId() + "' tidak ditemukan"));

        if (!Boolean.TRUE.equals(vendor.getIsActive())) {
            throw new com.crustercrew.exception.baseException.BusinessValidationException(
                    "Vendor '" + vendor.getCompanyName() + "' sudah tidak aktif");
        }

        VendorCatalogItem item = VendorCatalogItem.builder()
                .vendor(vendor)
                .sku(request.getSku())
                .name(request.getName())
                .category(request.getCategory())
                .unit(request.getUnit())
                .unitPrice(request.getUnitPrice())
                .stockAvailable(request.getStockAvailable())
                .isActive(true)
                .build();

        return VendorCatalogItemResponse.from(catalogItemRepository.save(item));
    }

    @Transactional
    public VendorCatalogItemResponse updateCatalogItem(Long id, UpdateCatalogItemRequest request) {
        VendorCatalogItem item = catalogItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Catalog item dengan ID '" + id + "' tidak ditemukan"));

        if (StringUtils.hasText(request.getName())) {
            item.setName(request.getName());
        }
        if (StringUtils.hasText(request.getCategory())) {
            item.setCategory(request.getCategory());
        }
        if (StringUtils.hasText(request.getUnit())) {
            item.setUnit(request.getUnit());
        }
        if (request.getUnitPrice() != null) {
            item.setUnitPrice(request.getUnitPrice());
        }
        if (request.getStockAvailable() != null) {
            item.setStockAvailable(request.getStockAvailable());
        }

        return VendorCatalogItemResponse.from(catalogItemRepository.save(item));
    }
}