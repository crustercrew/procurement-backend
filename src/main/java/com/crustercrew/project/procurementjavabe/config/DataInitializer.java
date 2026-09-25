package com.crustercrew.project.procurementjavabe.config;

import com.crustercrew.project.procurementjavabe.entity.*;
import com.crustercrew.project.procurementjavabe.entity.enums.UserRole;
import com.crustercrew.project.procurementjavabe.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentBudgetRepository budgetRepository;
    private final VendorRepository vendorRepository;
    private final VendorCatalogItemRepository catalogItemRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    @Transactional
    public void run(String @NonNull ... args) {
        if (userRepository.count() > 0) {
            log.info("Database already contains users. Skipping initial seed data.");
            return;
        }

        log.info("Seeding initial procurement development data...");

        // 1. Departments
        Departments itDept = new Departments();
        itDept.setName("Information Technology");
        itDept.setCostCenterCode("CC-IT-001");
        itDept = departmentRepository.save(itDept);

        Departments finDept = new Departments();
        finDept.setName("Finance & Accounting");
        finDept.setCostCenterCode("CC-FIN-001");
        finDept = departmentRepository.save(finDept);

        Departments procDept = new Departments();
        procDept.setName("Procurement Department");
        procDept.setCostCenterCode("CC-PROC-001");
        procDept = departmentRepository.save(procDept);

        Departments opsDept = new Departments();
        opsDept.setName("Operations & Warehouse");
        opsDept.setCostCenterCode("CC-OPS-001");
        opsDept = departmentRepository.save(opsDept);

        // 2. Department Budgets for current year
        int currentYear = LocalDate.now().getYear();

        DepartmentBudgets itBudget = new DepartmentBudgets();
        itBudget.setDepartment(itDept);
        itBudget.setFiscalYear(currentYear);
        itBudget.setAllocatedAmount(new BigDecimal("500000000.00")); // 500 Juta
        budgetRepository.save(itBudget);

        DepartmentBudgets finBudget = new DepartmentBudgets();
        finBudget.setDepartment(finDept);
        finBudget.setFiscalYear(currentYear);
        finBudget.setAllocatedAmount(new BigDecimal("250000000.00")); // 250 Juta
        budgetRepository.save(finBudget);

        DepartmentBudgets procBudget = new DepartmentBudgets();
        procBudget.setDepartment(procDept);
        procBudget.setFiscalYear(currentYear);
        procBudget.setAllocatedAmount(new BigDecimal("150000000.00")); // 150 Juta
        budgetRepository.save(procBudget);

        DepartmentBudgets opsBudget = new DepartmentBudgets();
        opsBudget.setDepartment(opsDept);
        opsBudget.setFiscalYear(currentYear);
        opsBudget.setAllocatedAmount(new BigDecimal("300000000.00")); // 300 Juta
        budgetRepository.save(opsBudget);

        // 3. Users (Default password: password123)
        String defaultPassword = passwordEncoder.encode("password123");

        User adminUser = new User();
        adminUser.setEmail("admin@procurement.com");
        adminUser.setPasswordHash(defaultPassword);
        adminUser.setFullName("System Administrator");
        adminUser.setRole(UserRole.CEO);
        adminUser.setDepartment(itDept);
        userRepository.save(adminUser);

        User requesterUser = new User();
        requesterUser.setEmail("requester@procurement.com");
        requesterUser.setPasswordHash(defaultPassword);
        requesterUser.setFullName("Budi Requester");
        requesterUser.setRole(UserRole.REQUESTER);
        requesterUser.setDepartment(itDept);
        userRepository.save(requesterUser);

        User managerUser = new User();
        managerUser.setEmail("manager@procurement.com");
        managerUser.setPasswordHash(defaultPassword);
        managerUser.setFullName("Siti Manager");
        managerUser.setRole(UserRole.MANAGER);
        managerUser.setDepartment(itDept);
        userRepository.save(managerUser);

        User financeUser = new User();
        financeUser.setEmail("finance@procurement.com");
        financeUser.setPasswordHash(defaultPassword);
        financeUser.setFullName("Dewi Finance");
        financeUser.setRole(UserRole.FINANCE);
        financeUser.setDepartment(finDept);
        userRepository.save(financeUser);

        User procurementUser = new User();
        procurementUser.setEmail("procurement@procurement.com");
        procurementUser.setPasswordHash(defaultPassword);
        procurementUser.setFullName("Rudi Procurement");
        procurementUser.setRole(UserRole.PROCUREMENT);
        procurementUser.setDepartment(procDept);
        userRepository.save(procurementUser);

        User warehouseUser = new User();
        warehouseUser.setEmail("warehouse@procurement.com");
        warehouseUser.setPasswordHash(defaultPassword);
        warehouseUser.setFullName("Joko Warehouse");
        warehouseUser.setRole(UserRole.WAREHOUSE);
        warehouseUser.setDepartment(opsDept);
        userRepository.save(warehouseUser);

        User vendorUser = new User();
        vendorUser.setEmail("vendor@procurement.com");
        vendorUser.setPasswordHash(defaultPassword);
        vendorUser.setFullName("Vendor Mitra Utama");
        vendorUser.setRole(UserRole.VENDOR);
        vendorUser.setDepartment(null);
        vendorUser = userRepository.save(vendorUser);

        // 4. Vendor Profile
        Vendor vendor = new Vendor();
        vendor.setUser(vendorUser);
        vendor.setCompanyName("PT Mega Teknologi Pratama");
        vendor.setTaxIdNpwp("01.234.567.8-901.000");
        vendor.setContactEmail("vendor@procurement.com");
        vendor.setIsActive(true);
        vendor = vendorRepository.save(vendor);

        // 5. Vendor Catalog Items
        VendorCatalogItem item1 = new VendorCatalogItem();
        item1.setVendor(vendor);
        item1.setSku("DELL-5440");
        item1.setName("Laptop Dell Latitude 5440 i7 16GB 512GB");
        item1.setCategory("Electronics");
        item1.setUnit("UNIT");
        item1.setUnitPrice(new BigDecimal("18500000.00"));
        item1.setStockAvailable(50);
        item1.setIsActive(true);
        catalogItemRepository.save(item1);

        VendorCatalogItem item2 = new VendorCatalogItem();
        item2.setVendor(vendor);
        item2.setSku("LG-27-4K");
        item2.setName("Monitor LG 27 Inch UltraFine 4K UHD");
        item2.setCategory("Electronics");
        item2.setUnit("UNIT");
        item2.setUnitPrice(new BigDecimal("4200000.00"));
        item2.setStockAvailable(30);
        item2.setIsActive(true);
        catalogItemRepository.save(item2);

        VendorCatalogItem item3 = new VendorCatalogItem();
        item3.setVendor(vendor);
        item3.setSku("CHAIR-ERGO-01");
        item3.setName("Kursi Kerja Ergonomis Mesh Breathable");
        item3.setCategory("Furniture");
        item3.setUnit("UNIT");
        item3.setUnitPrice(new BigDecimal("1750000.00"));
        item3.setStockAvailable(100);
        item3.setIsActive(true);
        catalogItemRepository.save(item3);

        VendorCatalogItem item4 = new VendorCatalogItem();
        item4.setVendor(vendor);
        item4.setSku("PAPER-A4-80");
        item4.setName("Kertas HVS A4 80gr PaperOne (1 Dus / 5 Rim)");
        item4.setCategory("Office Supplies");
        item4.setUnit("BOX");
        item4.setUnitPrice(new BigDecimal("245000.00"));
        item4.setStockAvailable(500);
        item4.setIsActive(true);
        catalogItemRepository.save(item4);

        log.info("Initial procurement seed data successfully loaded!");
    }
}
