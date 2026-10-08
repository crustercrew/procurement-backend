package com.crustercrew.requisitionservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePrRequest {
    private Long requesterId;
    private Long departmentId;
    private String justificationNote;
    private List<PrItemRequest> items;
}
