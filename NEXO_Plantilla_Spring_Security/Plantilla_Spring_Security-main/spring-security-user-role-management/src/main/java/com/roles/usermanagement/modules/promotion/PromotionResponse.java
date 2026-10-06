package com.roles.usermanagement.modules.promotion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PromotionResponse(
        Long id,
        String name,
        String description,
        BigDecimal discount,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        String status,
        int progress,
        List<ProductRef> products
) {
 public record ProductRef(Long id, String name) {}
}
