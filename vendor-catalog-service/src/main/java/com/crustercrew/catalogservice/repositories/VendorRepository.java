package com.crustercrew.catalogservice.repositories;

import com.crustercrew.catalogservice.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findById(Long id);
    Vendor findByTaxIdNpwp(String taxIdNpwp);
    Optional<Vendor> findByUserId(Long userId);
}