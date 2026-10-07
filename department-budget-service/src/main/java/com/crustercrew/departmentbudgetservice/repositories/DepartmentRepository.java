package com.crustercrew.departmentbudgetservice.repositories;

import com.crustercrew.departmentbudgetservice.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Page<Department> findByNameAndCostCenterCode(String name, String costCenterCode, Pageable pageable);
    Page<Department> findByName(String name, Pageable pageable);
    Page<Department> findByCostCenterCode(String costCenterCode, Pageable pageable);
}