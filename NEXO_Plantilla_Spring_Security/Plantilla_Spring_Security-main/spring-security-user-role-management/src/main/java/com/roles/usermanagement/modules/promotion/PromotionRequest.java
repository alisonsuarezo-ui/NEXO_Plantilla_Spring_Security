package com.roles.usermanagement.modules.promotion;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record PromotionRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 500) String description,

        @NotNull
        @DecimalMin("0.01")
        @DecimalMax("100.00")
        @Digits(integer = 3, fraction = 2)
        BigDecimal discount,

        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,

        @Size(max = 100) Set<@NotNull @Positive Long> productIds
) {}
