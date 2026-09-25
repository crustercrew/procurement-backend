package com.crustercrew.project.procurementjavabe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "department_budgets",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_dept_fiscal",
                        columnNames = {"department_id", "fiscal_year"}
                )
        },
        check = {
                @CheckConstraint(
                        name = "chk_budget_balance",
                        constraint = "reserved_amount + spent_amount <= allocated_amount")
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
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    @Column(name = "reserved_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal reservedAmount = BigDecimal.ZERO;

    @Column(name = "spent_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Departments department;
}
