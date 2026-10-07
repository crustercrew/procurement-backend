package com.crustercrew.departmentbudgetservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDepartmentRequest {

    @NotBlank(message = "Department name diperlukan")
    String name;

    @NotNull(message = "Department code diperlukan")
    String code;

    @Valid
    DepartmentBudgetRequest departmentBudget;
}
