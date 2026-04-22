package com.eduardocastro.user_service.infrastructure.web.dto.request;

import com.eduardocastro.user_service.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Pattern(regexp = "^\\d{11}$", message = "Phone must be 11 digits") String phone,
        @NotBlank @Email String email,
        @NotNull UserRole role
) {}
