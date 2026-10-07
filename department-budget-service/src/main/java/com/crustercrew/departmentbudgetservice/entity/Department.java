package com.crustercrew.departmentbudgetservice.entity;

import com.crustercrew.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "cost_center_code", nullable = false, unique = true, length = 50)
    private String costCenterCode;

    @Column(name = "is_active",nullable = false)
    @Builder.Default
    private boolean isActive = true;
//    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
//    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("department")
    @Builder.Default
    private List<DepartmentBudgets> departmentBudgets = new ArrayList<>();

//    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
//    private List<PurchaseRequisitions> purchaseRequisitions = new ArrayList<>();
}
