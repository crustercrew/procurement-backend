package com.crustercrew.departmentbudgetservice.repositories;

import com.crustercrew.departmentbudgetservice.entity.DepartmentBudgets;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentBudgetsRepository extends JpaRepository<DepartmentBudgets, Long> {
    DepartmentBudgets findByDepartmentIdAndFiscalYear(Long departmentId, Integer fiscalYear);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT db from DepartmentBudgets db where db.department.id = :departmentId and db.fiscalYear = :fiscalYear")
    Optional<DepartmentBudgets> findBudgetForUpdate(
            @Param("departmentId") Long departmentId,
            @Param("fiscalYear") Integer fiscalYear
    );
}