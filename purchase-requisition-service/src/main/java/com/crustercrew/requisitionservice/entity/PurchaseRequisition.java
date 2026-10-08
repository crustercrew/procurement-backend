package com.crustercrew.requisitionservice.entity;


import com.crustercrew.requisitionservice.entity.enums.PrStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "purchase_requisitions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String prNumber;
    private Long requesterId;
    private Long departmentId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrStatus status; // SUBMITTED, APPROVED, REJECTED
    private BigDecimal totalAmount;
    private String justificationNote;
    private String rejectionReason;
    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RequisitionItem> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
