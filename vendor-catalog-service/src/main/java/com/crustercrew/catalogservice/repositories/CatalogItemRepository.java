package com.crustercrew.catalogservice.repositories;

import com.crustercrew.catalogservice.entity.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "catalog-items", collectionResourceRel = "catalog-items")
public interface CatalogItemRepository extends JpaRepository<CatalogItem,Long> {
}
