package com.crustercrew.catalogservice.config;

import com.crustercrew.catalogservice.entity.CatalogItem;
import com.crustercrew.catalogservice.entity.Category;
import com.crustercrew.catalogservice.repositories.CatalogItemRepository;
import com.crustercrew.catalogservice.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoryRepository categoryRepository;
    private final CatalogItemRepository catalogItemRepository;

    @Bean
    public CommandLineRunner initCatalogData() {
        return args -> {
            if (categoryRepository.count() == 0) {
                Category itHardware = categoryRepository.save(Category.builder()
                        .name("IT Hardware & Equipment")
                        .description("Perangkat keras komputer dan periferal")
                        .build());

                Category officeSupplies = categoryRepository.save(Category.builder()
                        .name("Office Supplies")
                        .description("Alat tulis dan perlengkapan kantor")
                        .build());

                catalogItemRepository.save(CatalogItem.builder()
                        .vendorId(3L) // PT Dell Indonesia
                        .category(itHardware)
                        .sku("DELL-LAT-5440")
                        .name("Laptop Dell Latitude 5440 Core i7")
                        .description("RAM 16GB, SSD 512GB, Windows 11 Pro")
                        .unitPrice(new BigDecimal("18500000.00"))
                        .unitOfMeasure("UNIT")
                        .isActive(true)
                        .build());

                catalogItemRepository.save(CatalogItem.builder()
                        .vendorId(3L) // PT Dell Indonesia
                        .category(itHardware)
                        .sku("DELL-MON-P2422H")
                        .name("Monitor Dell 24 Inch P2422H")
                        .description("FHD IPS, Height Adjustable Stand")
                        .unitPrice(new BigDecimal("2800000.00"))
                        .unitOfMeasure("UNIT")
                        .isActive(true)
                        .build());

                System.out.println(">>> Sample Catalog Items & Categories berhasil di-seed!");
            }
        };
    }
}