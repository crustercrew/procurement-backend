package com.crustercrew.departmentbudgetservice.controllers;

import com.crustercrew.departmentbudgetservice.dto.request.BudgetReserveRequest;
import com.crustercrew.departmentbudgetservice.service.DepartmentBudgetService;
import com.crustercrew.dto.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Internal endpoints untuk operasi ledger budget. Tidak diekspos lewat API Gateway
 * (tidak ada route ke /internal/**) — dipanggil antar-service lewat Feign.
 */
@RestController
@RequestMapping("/internal/budgets")
@RequiredArgsConstructor
public class InternalBudgetController {

    private final DepartmentBudgetService departmentBudgetService;

    @PostMapping("/reserve")
    public ResponseEntity<APIResponse<Void>> reserveBudget(
            @Valid @RequestBody BudgetReserveRequest budgetReserveRequest
    ) {
        departmentBudgetService.reserveBudget(
                budgetReserveRequest.getDepartmentId(),
                budgetReserveRequest.getFiscalYear(),
                budgetReserveRequest.getAmount()
        );
        return ResponseEntity.ok(
                APIResponse.success("Alokasi budget berhasil di-reserve", null)
        );
    }

    @PostMapping("/release")
    public ResponseEntity<APIResponse<Void>> releaseBudget(
            @Valid @RequestBody BudgetReserveRequest budgetReserveRequest
    ) {
        departmentBudgetService.releaseBudget(
                budgetReserveRequest.getDepartmentId(),
                budgetReserveRequest.getFiscalYear(),
                budgetReserveRequest.getAmount()
        );
        return ResponseEntity.ok(
                APIResponse.success("Alokasi budget berhasil direlease", null)
        );
    }

    @PostMapping("/settle")
    public ResponseEntity<APIResponse<Void>> settleBudget(
            @Valid @RequestBody BudgetReserveRequest budgetReserveRequest
    ) {
        departmentBudgetService.settleBudget(
                budgetReserveRequest.getDepartmentId(),
                budgetReserveRequest.getFiscalYear(),
                budgetReserveRequest.getAmount()
        );
        return ResponseEntity.ok(
                APIResponse.success("Alokasi budget berhasil disetel", null)
        );
    }
}
