package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.GoodsReceipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {
    List<GoodsReceipt> findByPoId(Long poId);
    Page<GoodsReceipt> findByPoId(Long poId, Pageable pageable);
    GoodsReceipt findByReceiptNumber(String receiptNumber);
}
