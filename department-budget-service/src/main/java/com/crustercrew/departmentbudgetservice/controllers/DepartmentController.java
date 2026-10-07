package com.crustercrew.departmentbudgetservice.controllers;

import com.crustercrew.departmentbudgetservice.dto.request.CreateDepartmentRequest;
import com.crustercrew.departmentbudgetservice.entity.Department;
import com.crustercrew.departmentbudgetservice.entity.DepartmentBudgets;
import com.crustercrew.departmentbudgetservice.service.DepartmentBudgetService;
import com.crustercrew.departmentbudgetservice.service.DepartmentService;
import com.crustercrew.dto.APIResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @PageableDefault(size = 10, page = 0)
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
