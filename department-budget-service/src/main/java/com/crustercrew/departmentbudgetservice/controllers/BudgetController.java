package com.crustercrew.departmentbudgetservice.controllers;

import com.crustercrew.dto.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    @GetMapping("/fiscal-year")
    public ResponseEntity<APIResponse<Integer>> getCurrentFiscalYear() {
        return ResponseEntity.ok(
                APIResponse.success("Berhasil mendapatkan fiscal year", LocalDate.now().getYear())
        );
    }
}
