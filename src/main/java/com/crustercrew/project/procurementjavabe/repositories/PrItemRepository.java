package com.crustercrew.project.procurementjavabe.repositories;

import com.crustercrew.project.procurementjavabe.entity.PrItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrItemRepository extends JpaRepository<PrItem, Long>{
    List<PrItem> findByPrId(Long prId);
}
