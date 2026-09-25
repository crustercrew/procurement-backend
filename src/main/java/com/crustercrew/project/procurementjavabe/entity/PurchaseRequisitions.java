package com.crustercrew.project.procurementjavabe.entity;

import com.crustercrew.project.procurementjavabe.entity.enums.PrStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_requisitions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequisitions extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pr_number", nullable = false, unique = true, length = 60)
    private String prNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Departments department;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PrStatus status = PrStatus.DRAFT;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "target_fiscal_year", nullable = false)
    private Integer targetFiscalYear;

    @Column(name = "justification_note", columnDefinition = "TEXT")
    private String justificationNote;

    @OneToMany(mappedBy = "pr", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrItem> items = new ArrayList<>();
}
