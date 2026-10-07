package com.crustercrew.departmentbudgetservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepartmentBudgetRequest {

    @NotNull(message = "Fiscal year diperlukan")
    private Integer fiscalYear;

    @NotNull(message = "Allocated amount diperlukan")
    private BigDecimal allocatedAmount;

    @NotNull(message = "Reserved amount diperlukan")
    private BigDecimal reservedAmount ;

    @NotNull(message = "Spent amount diperlukan")
    private BigDecimal spentAmount;
}
