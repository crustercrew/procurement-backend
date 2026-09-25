package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.DepartmentBudgets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentBudgetRepository extends JpaRepository<DepartmentBudgets, Long> {
    DepartmentBudgets findByDepartmentIdAndFiscalYear(Long departmentId, Integer fiscalYear);

    @Query("SELECT db from DepartmentBudgets db where db.department.id = :departmentId and db.fiscalYear = :fiscalYear")
    DepartmentBudgets findBudgetForUpdate(
            @Param("departmentId") Long departmentId,
            @Param("fiscalYear") Integer fiscalYear
    );
}
