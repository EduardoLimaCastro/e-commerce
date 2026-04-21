package com.eduardocastro.user_service.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName
) {}
