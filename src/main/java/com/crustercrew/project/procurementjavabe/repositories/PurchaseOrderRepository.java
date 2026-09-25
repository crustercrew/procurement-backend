package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.PurchaseOrder;
import com.crustercrew.project.procurementjavabe.entity.enums.PoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>{
    PurchaseOrder findByPoNumber(String poNumber);
    PurchaseOrder findByPrId(Long prId);
    List<PurchaseOrder> findByVendorId(Long vendorId);
    List<PurchaseOrder> findByStatus(PoStatus status);
    List<PurchaseOrder> findByVendorIdAndStatus(Long vendorId, PoStatus status);
}
