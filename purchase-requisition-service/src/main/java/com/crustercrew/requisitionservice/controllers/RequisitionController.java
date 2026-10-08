package com.crustercrew.requisitionservice.controllers;

import com.crustercrew.requisitionservice.dto.request.CreatePrRequest;
import com.crustercrew.requisitionservice.dto.request.ReviewRequest;
import com.crustercrew.requisitionservice.entity.PurchaseRequisition;
import com.crustercrew.requisitionservice.services.RequisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requisitions")
@RequiredArgsConstructor
public class RequisitionController {

    private final RequisitionService requisitionService;

    @GetMapping
    public ResponseEntity<List<PurchaseRequisition>> getAllRequisitions() {
        return ResponseEntity.ok(requisitionService.getAllRequisitions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequisition> getRequisitionById(@PathVariable Long id) {
        return ResponseEntity.ok(requisitionService.getRequisitionById(id));
    }

    @PostMapping
    public ResponseEntity<PurchaseRequisition> createRequisition(@RequestBody CreatePrRequest request) {
        PurchaseRequisition created = requisitionService.createRequisition(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/review")
    public ResponseEntity<PurchaseRequisition> reviewRequisition(
            @PathVariable Long id,
            @RequestBody ReviewRequest request) {
        PurchaseRequisition reviewed = requisitionService.reviewRequisition(id, request);
        return ResponseEntity.ok(reviewed);
    }
}
