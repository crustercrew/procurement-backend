package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.GoodsReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GoodsReceiptItemRepository extends JpaRepository<GoodsReceiptItem,Long> {
    List<GoodsReceiptItem> findByReceiptId(Long receiptId);
    List<GoodsReceiptItem> findByPoItemId(Long poItemId);
}
