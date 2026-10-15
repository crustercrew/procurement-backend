package com.crustercrew.departmentbudgetservice.controllers;

import com.crustercrew.departmentbudgetservice.dto.request.DepartmentBudgetRequest;
import com.crustercrew.departmentbudgetservice.entity.Department;
import com.crustercrew.departmentbudgetservice.entity.DepartmentBudgets;
import com.crustercrew.departmentbudgetservice.service.DepartmentBudgetService;
import com.crustercrew.departmentbudgetservice.service.DepartmentService;
import com.crustercrew.dto.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/department-budgets")
@RequiredArgsConstructor
public class DepartmentBudgetController {

    private final DepartmentBudgetService departmentBudgetService;
    private final DepartmentService departmentService;

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
