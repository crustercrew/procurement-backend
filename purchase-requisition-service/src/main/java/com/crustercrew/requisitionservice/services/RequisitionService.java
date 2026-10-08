package com.crustercrew.requisitionservice.services;

import com.crustercrew.requisitionservice.client.CatalogFeignClient;
import com.crustercrew.requisitionservice.client.UserFeignClient;
import com.crustercrew.requisitionservice.dto.request.CreatePrRequest;
import com.crustercrew.requisitionservice.dto.request.PrItemRequest;
import com.crustercrew.requisitionservice.dto.request.ReviewRequest;
import com.crustercrew.requisitionservice.dto.response.CatalogItemResponse;
import com.crustercrew.requisitionservice.dto.response.UserResponse;
import com.crustercrew.requisitionservice.entity.PurchaseRequisition;
import com.crustercrew.requisitionservice.entity.RequisitionItem;
import com.crustercrew.requisitionservice.entity.enums.PrStatus;
import com.crustercrew.requisitionservice.repositories.PurchaseRequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequisitionService {

    private final PurchaseRequisitionRepository prRepository;
    private final CatalogFeignClient catalogFeignClient;
    private final UserFeignClient userFeignClient;

    public List<PurchaseRequisition> getAllRequisitions() {
        return prRepository.findAll();
    }

    public PurchaseRequisition getRequisitionById(Long id) {
        return prRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PR ID " + id + " tidak ditemukan"));
    }

    @Transactional
    public PurchaseRequisition createRequisition(CreatePrRequest request) {
        // validasi user
        UserResponse userResponse = userFeignClient.getUserById(request.getRequesterId());
        if (userResponse == null) {
            throw new RuntimeException("User ID " + request.getRequesterId() + " tidak ditemukan");
        }

        String prNumber = "PR-" + System.currentTimeMillis();

        PurchaseRequisition pr = PurchaseRequisition.builder()
                .prNumber(prNumber)
                .requesterId(request.getRequesterId())
                .departmentId(request.getDepartmentId())
                .status(PrStatus.SUBMITTED)
                .justificationNote(request.getJustificationNote())
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal grandTotal = BigDecimal.ZERO;
        for (PrItemRequest itemReq : request.getItems()) {
            CatalogItemResponse catalogItem = catalogFeignClient.getCatalogItem(itemReq.getCatalogItemId());
            if (catalogItem == null || Boolean.FALSE.equals(catalogItem.getIsActive())) {
                throw new RuntimeException("Catalog item ID " + itemReq.getCatalogItemId() + " tidak tersedia");
            }
            BigDecimal subtotal = catalogItem.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            RequisitionItem item = RequisitionItem.builder()
                    .requisition(pr)
                    .catalogItemId(itemReq.getCatalogItemId())
                    .vendorId(catalogItem.getVendorId())
                    .itemName(catalogItem.getName())
                    .quantity(itemReq.getQuantity())
                    .unitPrice(catalogItem.getUnitPrice())
                    .subtotal(subtotal)
                    .build();
            pr.getItems().add(item);
            grandTotal = grandTotal.add(subtotal);
        }
        pr.setTotalAmount(grandTotal);
        return prRepository.save(pr);
    }

    @Transactional
    public PurchaseRequisition reviewRequisition(Long prId, ReviewRequest request) {
        PurchaseRequisition pr = prRepository.findById(prId)
                .orElseThrow(() -> new RuntimeException("PR ID " + prId + " tidak ditemukan"));
        if (pr.getStatus() != PrStatus.SUBMITTED) {
            throw new IllegalStateException("Hanya PR dengan status SUBMITTED yang bisa di-review");
        }
        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            pr.setStatus(PrStatus.APPROVED);
        } else if ("REJECT".equalsIgnoreCase(request.getAction())) {
            pr.setStatus(PrStatus.REJECTED);
            pr.setRejectionReason(request.getRejectionReason());
        } else {
            throw new IllegalArgumentException("Action harus APPROVE atau REJECT");
        }
        return prRepository.save(pr);
    }
}
