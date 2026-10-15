package com.crustercrew.departmentbudgetservice.controllers;

import com.crustercrew.departmentbudgetservice.dto.request.CreateDepartmentRequest;
import com.crustercrew.departmentbudgetservice.dto.response.BudgetSummaryResponse;
import com.crustercrew.departmentbudgetservice.entity.Department;
import com.crustercrew.departmentbudgetservice.service.DepartmentBudgetService;
import com.crustercrew.departmentbudgetservice.service.DepartmentService;
import com.crustercrew.dto.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final DepartmentBudgetService departmentBudgetService;

    @GetMapping
    public ResponseEntity<APIResponse<Page<Department>>> getallDepartments(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "costCenterCode",required = false) String costCenterCode,
            @PageableDefault()
            Pageable pageable
    ) {
        Page<Department> page = departmentService.getAllDepartments(name, costCenterCode, pageable);
        return ResponseEntity.ok(
                APIResponse.success("Success get data",page)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Department>> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(
                APIResponse.success("Success get data",departmentService.getDepartmentById(id))
        );
    }

    @GetMapping("/{id}/budget")
    public ResponseEntity<APIResponse<BudgetSummaryResponse>> getDepartmentBudget(
            @PathVariable Long id,
            @RequestParam(value = "fiscalYear", required = false) Integer fiscalYear
    ) {
        Integer year = fiscalYear != null ? fiscalYear : LocalDate.now().getYear();
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan ringkasan budget",
                        departmentBudgetService.getBudgetSummary(id, year))
        );
    }

    @PostMapping
    public ResponseEntity<APIResponse<Department>> createDepartment(
            @Valid
            @RequestBody
            CreateDepartmentRequest departmentRequest
    ) {
        Department createdDepartment = departmentService.createDepartment(departmentRequest);
        return ResponseEntity.ok(
                APIResponse.success("Success create data", createdDepartment)
        );
    }
}
