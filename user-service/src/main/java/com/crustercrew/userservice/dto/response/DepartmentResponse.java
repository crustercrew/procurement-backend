package com.crustercrew.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {

    private Long id;

    private String name;

    private String costCenterCode;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private List<DepartmenBudgetsResponse> departmentBudgets = new ArrayList<>();
}