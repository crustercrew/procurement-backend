package com.crustercrew.project.procurementjavabe.entity;

import com.crustercrew.project.procurementjavabe.entity.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "vendor_invoices")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendorInvoice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(name = "invoice_number", nullable = false, unique = true, length = 100)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id", nullable = false)
    private PurchaseOrder po;

    @Column(name = "billed_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal billedAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_status", nullable = false, length = 50)
    private MatchStatus matchStatus;

    @Column(name = "is_paid")
    private Boolean isPaid = false;

    @Column(name = "discrepancy_note", columnDefinition = "TEXT")
    private String discrepancyNote = null;
}
