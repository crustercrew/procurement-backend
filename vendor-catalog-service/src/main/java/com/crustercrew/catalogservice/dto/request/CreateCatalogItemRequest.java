package com.crustercrew.catalogservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCatalogItemRequest {

    @NotNull(message = "Vendor ID diperlukan")
    private Long vendorId;

    @NotBlank(message = "SKU diperlukan")
    private String sku;

    @NotBlank(message = "Nama item diperlukan")
    private String name;

    @NotBlank(message = "Kategori diperlukan")
    private String category;

    @NotBlank(message = "Satuan diperlukan")
    private String unit;

    @NotNull(message = "Harga satuan diperlukan")
    @DecimalMin(value = "0.01", message = "Harga satuan harus lebih dari 0")
    private BigDecimal unitPrice;

    @NotNull(message = "Stok diperlukan")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stockAvailable;
}