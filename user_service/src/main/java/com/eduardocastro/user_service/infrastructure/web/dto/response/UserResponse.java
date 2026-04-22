package com.eduardocastro.user_service.infrastructure.web.dto.response;

import com.eduardocastro.user_service.domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String phone,
        String email,
        AddressResponse address,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record AddressResponse(
            String street,
            String number,
            String complement,
            String neighborhood,
            String city,
            String state,
            String zipCode
    ) {}
}
