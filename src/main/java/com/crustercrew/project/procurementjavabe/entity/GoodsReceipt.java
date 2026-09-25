package com.crustercrew.project.procurementjavabe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "goods_receipts")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_number", nullable = false, unique = true, length = 60)
    private String receiptNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id", nullable = false)
    private PurchaseOrder po;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by", nullable = false)
    private User receivedBy;

    @Builder.Default
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt = LocalDateTime.now();

    @Builder.Default
    @OneToMany(mappedBy = "receipt", fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<GoodsReceiptItem> items = new ArrayList<>();
}
