package com.crustercrew.project.procurementjavabe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "vendor_catalog_items",
        uniqueConstraints = {@UniqueConstraint(name = "uq_vendor_sku", columnNames = {"vendor_id", "sku"})}
        )
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorCatalogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false, length = 60)
    private String sku;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 30)
    private String unit; // PCS, BOX, UNIT

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "stock_available", nullable = false)
    private Integer stockAvailable;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
