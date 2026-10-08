package com.crustercrew.catalogservice.dto.response;

import com.crustercrew.catalogservice.entity.VendorCatalogItem;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorCatalogItemResponse {
    private Long id;
    private Long vendorId;
    private String vendorCompanyName;
    private String sku;
    private String name;
    private String category;
    private String unit;
    private BigDecimal unitPrice;
    private Integer stockAvailable;
    private Boolean isActive;

    public static VendorCatalogItemResponse from(VendorCatalogItem item) {
        return VendorCatalogItemResponse.builder()
                .id(item.getId())
                .vendorId(item.getVendor() != null ? item.getVendor().getId() : null)
                .vendorCompanyName(item.getVendor() != null ? item.getVendor().getCompanyName() : null)
                .sku(item.getSku())
                .name(item.getName())
                .category(item.getCategory())
                .unit(item.getUnit())
                .unitPrice(item.getUnitPrice())
                .stockAvailable(item.getStockAvailable())
                .isActive(item.getIsActive())
                .build();
    }
}
