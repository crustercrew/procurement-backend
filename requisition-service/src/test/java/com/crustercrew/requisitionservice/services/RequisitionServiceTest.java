package com.crustercrew.requisitionservice.services;

import com.crustercrew.requisitionservice.client.CatalogFeignClient;
import com.crustercrew.requisitionservice.client.UserFeignClient;
import com.crustercrew.requisitionservice.dto.request.CreatePrRequest;
import com.crustercrew.requisitionservice.dto.request.PrItemRequest;
import com.crustercrew.requisitionservice.dto.request.ReviewRequest;
import com.crustercrew.requisitionservice.dto.response.CatalogItemResponse;
import com.crustercrew.requisitionservice.dto.response.UserResponse;
import com.crustercrew.requisitionservice.entity.PurchaseRequisition;
import com.crustercrew.requisitionservice.entity.enums.PrStatus;
import com.crustercrew.requisitionservice.repositories.PurchaseRequisitionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequisitionService Unit Tests")
class RequisitionServiceTest {

    @Mock
    private PurchaseRequisitionRepository prRepository;

    @Mock
    private CatalogFeignClient catalogFeignClient;

    @Mock
    private UserFeignClient userFeignClient;

    @InjectMocks
    private RequisitionService requisitionService;

    @Test
    @DisplayName("Create PR: Berhasil menghitung total dan memanggil Feign Client")
    void testCreateRequisition_Success() {
        // Arrange
        CreatePrRequest request = new CreatePrRequest();
        request.setRequesterId(1L);
        request.setDepartmentId(1L);
        request.setJustificationNote("Pengadaan Laptop Developer");

        PrItemRequest itemRequest = new PrItemRequest();
        itemRequest.setCatalogItemId(1L);
        itemRequest.setQuantity(2);
        request.setItems(List.of(itemRequest));

        UserResponse mockUser = UserResponse.builder()
                .id(1L)
                .username("requester")
                .fullName("Staff Pengadaan")
                .role("REQUESTER")
                .departmentId(1L)
                .build();
        when(userFeignClient.getUserById(1L)).thenReturn(mockUser);

        CatalogItemResponse mockCatalog = CatalogItemResponse.builder()
                .id(1L)
                .name("Laptop Dell Latitude 5420")
                .unitPrice(new BigDecimal("18500000"))
                .vendorId(3L)
                .isActive(true)
                .build();
        when(catalogFeignClient.getCatalogItem(1L)).thenReturn(mockCatalog);

        when(prRepository.save(any(PurchaseRequisition.class))).thenAnswer(invocation -> {
            PurchaseRequisition saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        // Act
        PurchaseRequisition result = requisitionService.createRequisition(request);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(PrStatus.SUBMITTED, result.getStatus());
        assertEquals(new BigDecimal("37000000"), result.getTotalAmount());
        assertEquals(1, result.getItems().size());
        assertEquals("Laptop Dell Latitude 5420", result.getItems().get(0).getItemName());

        verify(userFeignClient, times(1)).getUserById(1L);
        verify(catalogFeignClient, times(1)).getCatalogItem(1L);
        verify(prRepository, times(1)).save(any(PurchaseRequisition.class));
    }

    @Test
    @DisplayName("Create PR: Gagal jika User Requester tidak ditemukan")
    void testCreateRequisition_UserNotFound() {
        CreatePrRequest request = new CreatePrRequest();
        request.setRequesterId(999L);
        request.setItems(List.of());

        when(userFeignClient.getUserById(999L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            requisitionService.createRequisition(request);
        });

        assertTrue(exception.getMessage().contains("tidak ditemukan"));
        verify(prRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create PR: Gagal jika Item Katalog tidak aktif")
    void testCreateRequisition_CatalogItemInactive() {
        CreatePrRequest request = new CreatePrRequest();
        request.setRequesterId(1L);
        PrItemRequest itemRequest = new PrItemRequest();
        itemRequest.setCatalogItemId(2L);
        itemRequest.setQuantity(1);
        request.setItems(List.of(itemRequest));

        UserResponse mockUser = UserResponse.builder()
                .id(1L)
                .username("requester")
                .build();
        when(userFeignClient.getUserById(1L)).thenReturn(mockUser);

        CatalogItemResponse inactiveItem = CatalogItemResponse.builder()
                .id(2L)
                .name("Barang Discontinue")
                .unitPrice(new BigDecimal("5000000"))
                .isActive(false)
                .build();
        when(catalogFeignClient.getCatalogItem(2L)).thenReturn(inactiveItem);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            requisitionService.createRequisition(request);
        });

        assertTrue(exception.getMessage().contains("tidak tersedia"));
        verify(prRepository, never()).save(any());
    }

    @Test
    @DisplayName("Review PR: Manager berhasil Approve PR")
    void testReviewRequisition_ApproveSuccess() {
        PurchaseRequisition pr = PurchaseRequisition.builder()
                .id(1L)
                .status(PrStatus.SUBMITTED)
                .totalAmount(new BigDecimal("10000000"))
                .build();

        when(prRepository.findById(1L)).thenReturn(Optional.of(pr));
        when(prRepository.save(any(PurchaseRequisition.class))).thenAnswer(i -> i.getArgument(0));

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setAction("APPROVE");

        PurchaseRequisition result = requisitionService.reviewRequisition(1L, reviewRequest);

        assertNotNull(result);
        assertEquals(PrStatus.APPROVED, result.getStatus());
        verify(prRepository, times(1)).save(pr);
    }

    @Test
    @DisplayName("Review PR: Manager berhasil Reject PR dengan alasan")
    void testReviewRequisition_RejectSuccess() {
        PurchaseRequisition pr = PurchaseRequisition.builder()
                .id(1L)
                .status(PrStatus.SUBMITTED)
                .totalAmount(new BigDecimal("10000000"))
                .build();

        when(prRepository.findById(1L)).thenReturn(Optional.of(pr));
        when(prRepository.save(any(PurchaseRequisition.class))).thenAnswer(i -> i.getArgument(0));

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setAction("REJECT");
        reviewRequest.setRejectionReason("Pagu anggaran departemen telah habis");

        PurchaseRequisition result = requisitionService.reviewRequisition(1L, reviewRequest);

        assertNotNull(result);
        assertEquals(PrStatus.REJECTED, result.getStatus());
        assertEquals("Pagu anggaran departemen telah habis", result.getRejectionReason());
        verify(prRepository, times(1)).save(pr);
    }
}
