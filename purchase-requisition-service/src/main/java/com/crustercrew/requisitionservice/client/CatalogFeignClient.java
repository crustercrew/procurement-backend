package com.crustercrew.requisitionservice.client;

import com.crustercrew.requisitionservice.dto.response.CatalogItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vendor-catalog-service")
public interface CatalogFeignClient {
    @GetMapping("/catalog-items/{id}")
    CatalogItemResponse getCatalogItem(@PathVariable("id") Long id);
}

