package com.roles.usermanagement.modules.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 300) String description,
        @Size(max = 50) String icon
) {}
