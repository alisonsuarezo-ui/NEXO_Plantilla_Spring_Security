package com.roles.usermanagement.modules.paymentmethod;

public record PaymentMethodResponse(
        Long id,
        String name,
        String description,
        String icon,
        boolean active
) {}
