package com.roles.usermanagement.modules.category;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        String icon,
        boolean active,
        long productCount
) {}
