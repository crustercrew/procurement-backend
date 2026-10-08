package com.crustercrew.catalogservice.entity;

import com.crustercrew.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendors")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vendor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = true,unique = true)
    private Long userId;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "tax_id_npwp", nullable = false, unique = true, length = 50)
    private String taxIdNpwp;

    @Column(name = "contact_email", nullable = false, length = 100)
    private String contactEmail;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<VendorCatalogItem> catalogItems = new ArrayList<>();

//    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
//    private List<PurchaseOrder> purchaseOrders= new ArrayList<>();
}
