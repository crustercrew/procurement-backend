package com.crustercrew.departmentbudgetservice.service;

import com.crustercrew.departmentbudgetservice.dto.response.BudgetSummaryResponse;
import com.crustercrew.departmentbudgetservice.entity.DepartmentBudgets;
import com.crustercrew.departmentbudgetservice.repositories.DepartmentBudgetsRepository;
import com.crustercrew.exception.baseException.InsufficientBudgetException;
import com.crustercrew.exception.baseException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepartmentBudgetService {
    private final DepartmentBudgetsRepository departmentBudgetsRepository;

    @Transactional
    public void reserveBudget(
            Long departmentId,
            Integer fiscalYear,
            BigDecimal amount
    ){
        DepartmentBudgets budgets = departmentBudgetsRepository
                .findBudgetForUpdate(departmentId, fiscalYear)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Department budget not found")
                );

        BigDecimal remainingBudget = budgets.getAllocatedAmount().subtract(budgets.getReservedAmount().add(budgets.getSpentAmount()));

        if (remainingBudget.compareTo(amount) < 0) {
            throw new InsufficientBudgetException("Anggaran divisi tidak mencukupi. Dibutuhkan: Rp " + amount + ". Sisa: Rp " + remainingBudget);
        }

        budgets.setReservedAmount(budgets.getReservedAmount().add(amount));

        departmentBudgetsRepository.save(budgets);
    }

    @Transactional
    public void releaseBudget(Long departmentId, Integer fiscalYear, BigDecimal amount){
        DepartmentBudgets budgets = departmentBudgetsRepository
                .findBudgetForUpdate(departmentId, fiscalYear)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Department budget not found")
                );

        // Guard: reserved_amount tidak boleh negatif
        BigDecimal newReserved = budgets.getReservedAmount().subtract(amount);
        if(newReserved.compareTo(BigDecimal.ZERO) < 0){
            newReserved = BigDecimal.ZERO;
        }

        budgets.setReservedAmount(newReserved);

        departmentBudgetsRepository.save(budgets);
    }

    @Transactional
    public void settleBudget(Long departmentId, Integer fiscalYear, BigDecimal amount) {
        DepartmentBudgets budget = departmentBudgetsRepository
                .findBudgetForUpdate(departmentId, fiscalYear)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Department budget not found")
                );

        budget.setReservedAmount(budget.getReservedAmount().subtract(amount));
        budget.setSpentAmount(budget.getSpentAmount().add(amount));

        // Guard: reserved_amount tidak boleh negatif
        if(budget.getReservedAmount().compareTo(BigDecimal.ZERO) < 0){
            budget.setReservedAmount(BigDecimal.ZERO);
        }

        departmentBudgetsRepository.save(budget);
    }

    public DepartmentBudgets createDepartmentBudgets(DepartmentBudgets departmentBudgets){
        return departmentBudgetsRepository.save(departmentBudgets);
    }

    @Transactional(readOnly = true)
    public BudgetSummaryResponse getBudgetSummary(Long departmentId, Integer fiscalYear){
        DepartmentBudgets budget = departmentBudgetsRepository
                .findByDepartmentIdAndFiscalYear(departmentId, fiscalYear);

        if (budget == null) {
            throw new ResourceNotFoundException(
                    "Department budget tidak ditemukan untuk department id: " + departmentId
                            + " fiscal year: " + fiscalYear);
        }

        BigDecimal remaining = budget.getAllocatedAmount()
                .subtract(budget.getReservedAmount().add(budget.getSpentAmount()));

        return BudgetSummaryResponse.builder()
                .departmentId(budget.getDepartment().getId())
                .departmentName(budget.getDepartment().getName())
                .fiscalYear(budget.getFiscalYear())
                .allocatedAmount(budget.getAllocatedAmount())
                .reservedAmount(budget.getReservedAmount())
                .spentAmount(budget.getSpentAmount())
                .remainingAmount(remaining)
                .build();
    }
}
