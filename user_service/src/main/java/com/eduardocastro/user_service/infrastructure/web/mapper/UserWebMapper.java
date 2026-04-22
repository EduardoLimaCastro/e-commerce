package com.eduardocastro.user_service.infrastructure.web.mapper;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.valueobject.Address;
import com.eduardocastro.user_service.infrastructure.web.dto.request.CreateUserRequest;
import com.eduardocastro.user_service.infrastructure.web.dto.response.UserResponse;

public class UserWebMapper {

    private UserWebMapper() {}

    public static Address toAddress(CreateUserRequest.AddressRequest request) {
        return new Address(
                request.street(),
                request.number(),
                request.complement(),
                request.neighborhood(),
                request.city(),
                request.state(),
                request.zipCode()
        );
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone().getValue(),
                user.getEmail().getValue(),
                toAddressResponse(user.getAddress()),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    private static UserResponse.AddressResponse toAddressResponse(Address address) {
        return new UserResponse.AddressResponse(
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
