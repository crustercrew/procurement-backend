package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.VendorInvoice;
import com.crustercrew.project.procurementjavabe.entity.enums.MatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorInvoiceRepository extends JpaRepository<VendorInvoice, Long> {
    List<VendorInvoice> findByPoId(Long poId);
    Page<VendorInvoice> findByPoId(Long poId, Pageable pageable);
    Page<VendorInvoice> findByMatchStatus(MatchStatus matchStatus, Pageable pageable);
    VendorInvoice findByInvoiceNumber(String invoiceNumber);
}
