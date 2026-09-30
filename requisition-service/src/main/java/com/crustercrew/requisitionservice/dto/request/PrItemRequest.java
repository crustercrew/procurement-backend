package com.crustercrew.requisitionservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrItemRequest {
    private Long catalogItemId;
    private Integer quantity;
}
