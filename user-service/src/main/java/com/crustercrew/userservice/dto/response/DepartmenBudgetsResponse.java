package com.crustercrew.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmenBudgetsResponse {

    private Long id;

    private Integer fiscalYear;

    @Builder.Default
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal reservedAmount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal spentAmount = BigDecimal.ZERO;

    private DepartmentResponse department;
}