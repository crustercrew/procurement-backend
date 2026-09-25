package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.PurchaseRequisitions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisitions, Long>,
        JpaSpecificationExecutor<PurchaseRequisitions> {

    PurchaseRequisitions findByPrNumber(String prNumber);
}
