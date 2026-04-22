package com.eduardocastro.user_service.infrastructure.web.dto.response;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.valueobject.Address;

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
    ) {
        public static AddressResponse from(Address address) {
            return new AddressResponse(
                    address.getStreet(),
                    address.getNumber(),
                    address.getComplement(),
                    address.getNeighborhood(),
                    address.getCity(),
                    address.getState(),
                    address.getZipCode()
            );
        }
    }

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone().getValue(),
                user.getEmail().getValue(),
                AddressResponse.from(user.getAddress()),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
