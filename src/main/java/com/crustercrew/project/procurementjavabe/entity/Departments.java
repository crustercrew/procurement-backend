package com.crustercrew.project.procurementjavabe.entity;

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
public class Departments extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "cost_center_code", nullable = false, unique = true, length = 50)
    private String costCenterCode;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<DepartmentBudgets> departmentBudgets = new ArrayList<>();

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<PurchaseRequisitions> purchaseRequisitions = new ArrayList<>();
}
