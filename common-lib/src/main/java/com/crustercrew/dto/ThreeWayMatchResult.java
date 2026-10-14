package com.crustercrew.dto;

import com.crustercrew.enums.MatchStatus;

import java.math.BigDecimal;

public record ThreeWayMatchResult(
        Long invoiceId,
        Long purchaseOrderId,
        MatchStatus matchStatus,
        boolean quantityMatched,
        boolean amountMatched,
        BigDecimal billedAmount,
        BigDecimal purchaseOrderTotal,
        String discrepancyNote
) {
}
