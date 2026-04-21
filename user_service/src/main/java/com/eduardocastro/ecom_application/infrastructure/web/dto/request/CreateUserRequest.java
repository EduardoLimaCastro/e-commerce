package com.eduardocastro.ecom_application.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName
) {}
