package com.crustercrew.userservice.integrations;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.crustercrew.dto.APIResponse;
import com.crustercrew.userservice.dto.response.DepartmentResponse;

@FeignClient(name = "department-budget-service")
public interface DepartmenServicesAPI {
    @GetMapping("/api/v1/departments/{id}")
    APIResponse<DepartmentResponse> getDepartmentById(@PathVariable("id") Long id);
}
