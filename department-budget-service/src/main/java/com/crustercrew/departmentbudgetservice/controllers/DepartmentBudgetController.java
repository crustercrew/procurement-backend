package com.crustercrew.departmentbudgetservice.controllers;

import com.crustercrew.departmentbudgetservice.dto.request.BudgetReserveRequest;
import com.crustercrew.departmentbudgetservice.dto.request.DepartmentBudgetRequest;
import com.crustercrew.departmentbudgetservice.entity.Department;
import com.crustercrew.departmentbudgetservice.entity.DepartmentBudgets;
import com.crustercrew.departmentbudgetservice.service.DepartmentBudgetService;
import com.crustercrew.departmentbudgetservice.service.DepartmentService;
import com.crustercrew.dto.APIResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/department-budgets")
@RequiredArgsConstructor
public class DepartmentBudgetController {

    private final DepartmentBudgetService departmentBudgetService;
    private final DepartmentService departmentService;

    @PostMapping("/reserve")
    public ResponseEntity<APIResponse<Void>> reserveBudget (
            @Valid
            @RequestBody
            BudgetReserveRequest budgetReserveRequest
    ){
        departmentBudgetService.reserveBudget(
                budgetReserveRequest.getDepartmentId(),
                budgetReserveRequest.getFiscalYear(),
                budgetReserveRequest.getAmount()
        );
        return ResponseEntity.ok(
                APIResponse.success("Alokasi budget berhasil di-reserve",null)
        );
    }

    @PostMapping("/release")
    public ResponseEntity<APIResponse<Void>> releaseBudget(
            @Valid
            @RequestBody
            BudgetReserveRequest budgetReserveRequest
    ){
        departmentBudgetService.releaseBudget(
                budgetReserveRequest.getDepartmentId(),
                budgetReserveRequest.getFiscalYear(),
                budgetReserveRequest.getAmount()
        );
        return ResponseEntity.ok(
                APIResponse.success("Alokasi budget berhasil direlease",null)
        );
    }

    @PostMapping("/settle")
    public ResponseEntity<APIResponse<Void>> settleBudget(
            @Valid
            @RequestBody
            BudgetReserveRequest budgetReserveRequest
    ){
        departmentBudgetService.settleBudget(
                budgetReserveRequest.getDepartmentId(),
                budgetReserveRequest.getFiscalYear(),
                budgetReserveRequest.getAmount()
        );
        return ResponseEntity.ok(
                APIResponse.success("Alokasi budget berhasil disetel",null)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse<DepartmentBudgets>> createDepartmentBudgets(
            @RequestParam("departmentId")
            Long departmentId,
            @Valid
            @RequestBody
            DepartmentBudgetRequest departmentBudgets
    ){
        Department department = departmentService.getDepartmentById(departmentId);
        DepartmentBudgets createdDepartmentBudgets = departmentBudgetService.createDepartmentBudgets(
                DepartmentBudgets.builder()
                        .fiscalYear(departmentBudgets.getFiscalYear())
                        .allocatedAmount(departmentBudgets.getAllocatedAmount())
                        .reservedAmount(departmentBudgets.getReservedAmount())
                        .spentAmount(departmentBudgets.getSpentAmount())
                        .department(department)
                        .build()
        );
        return ResponseEntity.ok(
                APIResponse.success("Department budgets berhasil dibuat",createdDepartmentBudgets)
        );
    }
}
