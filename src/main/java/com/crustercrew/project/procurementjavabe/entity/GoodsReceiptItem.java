package com.crustercrew.project.procurementjavabe.entity;

import com.crustercrew.project.procurementjavabe.entity.enums.ConditionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "goods_receipt_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private GoodsReceipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_item_id", nullable = false)
    private PoItem poItem;

    @Column(name = "quantity_received", nullable = false)
    private Integer quantityReceived;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ConditionStatus condition;
}
