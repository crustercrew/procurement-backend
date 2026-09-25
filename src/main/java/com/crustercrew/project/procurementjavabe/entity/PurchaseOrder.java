package com.crustercrew.project.procurementjavabe.entity;

import com.crustercrew.project.procurementjavabe.entity.enums.PoStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "po_number", nullable = false, unique = true, length = 60)
    private String poNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pr_id", unique = true, nullable = false)
    private PurchaseRequisitions pr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PoStatus status;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "po", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "po", fetch = FetchType.LAZY)
    private List<GoodsReceipt> goodsReceipts= new ArrayList<>();

    @OneToMany(mappedBy = "po", fetch = FetchType.LAZY)
    private List<VendorInvoice> invoices = new ArrayList<>();
}
