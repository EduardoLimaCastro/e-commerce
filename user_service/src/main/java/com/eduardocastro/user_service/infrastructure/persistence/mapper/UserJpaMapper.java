package com.eduardocastro.user_service.infrastructure.persistence.mapper;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.valueobject.Address;
import com.eduardocastro.user_service.infrastructure.persistence.jpa.AddressEmbeddable;
import com.eduardocastro.user_service.infrastructure.persistence.jpa.UserJpaEntity;

public class UserJpaMapper {

    private UserJpaMapper() {}

    public static UserJpaEntity toEntity(User user) {
        Address a = user.getAddress();
        return new UserJpaEntity(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone().getValue(),
                user.getEmail().getValue(),
                new AddressEmbeddable(a.getStreet(), a.getNumber(), a.getComplement(), a.getNeighborhood(), a.getCity(), a.getState(), a.getZipCode()),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static User toDomain(UserJpaEntity entity) {
        AddressEmbeddable a = entity.getAddress();
        Address address = new Address(a.getStreet(), a.getNumber(), a.getComplement(), a.getNeighborhood(), a.getCity(), a.getState(), a.getZipCode());
        return User.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getEmail(),
                address,
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
