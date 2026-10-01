package com.crustercrew.catalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogItemRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal unitPrice;
    private String unit;
    private String unitOfMeasure;
    private Long categoryId;
    private Long vendorId;
    private String sku;
    @Builder.Default
    private Boolean isActive = true;
}
