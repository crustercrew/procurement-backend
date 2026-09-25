package com.crustercrew.project.procurementjavabe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vendor extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = true)
    private User user= null;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "tax_id_npwp", nullable = false, unique = true, length = 50)
    private String taxIdNpwp;

    @Column(name = "contact_email", nullable = false, length = 100)
    private String contactEmail;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private List<VendorCatalogItem> catalogItems = new ArrayList<>();

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private List<PurchaseOrder> purchaseOrders= new ArrayList<>();
}
