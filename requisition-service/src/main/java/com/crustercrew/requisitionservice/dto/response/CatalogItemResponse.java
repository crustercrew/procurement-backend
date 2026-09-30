package com.crustercrew.requisitionservice.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CatalogItemResponse {

    private Long id;

    private Long vendorId;

    private Object category;

    private String sku;

    private String name;

    private String description;

    private BigDecimal unitPrice;

    private String unitOfMeasure;

    @Builder.Default
    private Boolean isActive = true;
}
