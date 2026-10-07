package com.crustercrew.departmentbudgetservice.entity;

import com.crustercrew.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "department_budgets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "department_budgets_fiscal",
                        columnNames = {"department_id", "fiscal_year"}
                )
        },
        check = {
                @CheckConstraint(
                        name = "check_budget_balance",
                        constraint = "reserved_amount + spent_amount <= allocated_amount"
                )
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentBudgets extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "allocated_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    @Column(name = "reserved_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal reservedAmount = BigDecimal.ZERO;

    @Column(name = "spent_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonIgnoreProperties("departmentBudgets")
    private Department department;
}
