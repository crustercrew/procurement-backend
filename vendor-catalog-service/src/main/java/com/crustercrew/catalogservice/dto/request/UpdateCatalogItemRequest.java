package com.crustercrew.catalogservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateCatalogItemRequest {

    private String name;

    private String category;

    private String unit;

    @DecimalMin(value = "0.01", message = "Harga satuan harus lebih dari 0")
    private BigDecimal unitPrice;

    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stockAvailable;
}