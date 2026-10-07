package com.crustercrew.departmentbudgetservice.service;

import com.crustercrew.departmentbudgetservice.dto.request.CreateDepartmentRequest;
import com.crustercrew.departmentbudgetservice.dto.request.DepartmentBudgetRequest;
import com.crustercrew.departmentbudgetservice.entity.Department;
import com.crustercrew.departmentbudgetservice.entity.DepartmentBudgets;
import com.crustercrew.departmentbudgetservice.repositories.DepartmentBudgetsRepository;
import com.crustercrew.departmentbudgetservice.repositories.DepartmentRepository;
import com.crustercrew.exception.baseException.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentBudgetsRepository departmentBudgetsRepository;

    public Page<Department> getAllDepartments(
            String name,
            String costCenterCode,
            Pageable pageable
    ){
        if(name != null && costCenterCode != null){
            return departmentRepository.findByNameAndCostCenterCode(name, costCenterCode, pageable);
        } else if(name != null){
            return departmentRepository.findByName(name, pageable);
        } else if(costCenterCode != null){
            return departmentRepository.findByCostCenterCode(costCenterCode, pageable);
        } else {
            return departmentRepository.findAll(pageable);
        }
    }

    public Department getDepartmentById(Long id){
        return departmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Department tidak ditemukan dengan id: " + id)
        );
    }

    @Transactional
    public Department createDepartment(CreateDepartmentRequest request){
        Department department = Department.builder()
                .name(request.getName())
                .costCenterCode(request.getCode())
                .build();
        Department savedDepartment = departmentRepository.save(department);
        if (request.getDepartmentBudget() != null) {
            DepartmentBudgetRequest budgetReq = request.getDepartmentBudget();
            DepartmentBudgets budget = DepartmentBudgets.builder()
                    .department(savedDepartment)
                    .fiscalYear(budgetReq.getFiscalYear())
                    .allocatedAmount(budgetReq.getAllocatedAmount())
                    .reservedAmount(budgetReq.getReservedAmount() != null ? budgetReq.getReservedAmount() : BigDecimal.ZERO)
                    .spentAmount(budgetReq.getSpentAmount() != null ? budgetReq.getSpentAmount() : BigDecimal.ZERO)
                    .build();
            departmentBudgetsRepository.save(budget);
            savedDepartment.getDepartmentBudgets().add(budget);
        }
        return savedDepartment;
    }

    public Department updateDepartment(Long id, Department department){
        Department existingDepartment = departmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Department tidak ditemukan dengan id: " + id)
        );
        existingDepartment.setName(department.getName());
        existingDepartment.setDepartmentBudgets(department.getDepartmentBudgets());
        return departmentRepository.save(existingDepartment);
    }

    public Department closeDepartment(Long id){
        Department existingDepartment = departmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Department tidak ditemukan dengan id: " + id)
        );
        existingDepartment.setActive(false);
        return departmentRepository.save(existingDepartment);
    }
}
