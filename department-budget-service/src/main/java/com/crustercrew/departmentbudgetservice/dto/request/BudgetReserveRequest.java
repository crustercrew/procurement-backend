package com.crustercrew.departmentbudgetservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetReserveRequest {
    @NotNull(message = "Department ID dibutuhkan")
    private Long departmentId;
    @NotNull(message = "fiscal year dibutuhkan")
    private Integer fiscalYear;
    @NotNull(message = "amount dibutuhkan")
    private BigDecimal amount;
}
