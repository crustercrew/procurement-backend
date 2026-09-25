package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.VendorCatalogItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorCatalogItemRepository extends JpaRepository<VendorCatalogItem, Long> {
    Page<VendorCatalogItem> findByIsActiveTrue(Pageable pageable);
    Page<VendorCatalogItem> findByVendorIdAndIsActiveTrue(Long vendorId,Pageable pageable);
    Page<VendorCatalogItem> findByCategoryAndIsActiveTrue(String category,Pageable  pageable);

    @Query("""
        SELECT v FROM VendorCatalogItem v 
        WHERE v.isActive = true 
        AND (LOWER(v.name) LIKE LOWER(CONCAT('%', :keyword, '%')) 
             OR LOWER(v.sku) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<VendorCatalogItem> searchByKeyword(String keyword,Pageable pageable);
}
