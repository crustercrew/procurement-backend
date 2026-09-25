package com.crustercrew.project.procurementjavabe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "po_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id", nullable = false)
    private PurchaseOrder po;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_item_id", nullable = false)
    private VendorCatalogItem catalogItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;

    @OneToMany(mappedBy = "poItem", fetch = FetchType.LAZY)
    private List<GoodsReceiptItem> goodsReceiptItems= new ArrayList<>();
}
