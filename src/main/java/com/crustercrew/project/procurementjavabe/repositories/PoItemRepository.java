package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.PoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoItemRepository extends JpaRepository<PoItem, Long>{
    List<PoItem> findByPoId(Long poId);
}
