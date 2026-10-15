package com.crustercrew.departmentbudgetservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSummaryResponse {

    private Long departmentId;
    private String departmentName;
    private Integer fiscalYear;
    private BigDecimal allocatedAmount;
    private BigDecimal reservedAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
}
