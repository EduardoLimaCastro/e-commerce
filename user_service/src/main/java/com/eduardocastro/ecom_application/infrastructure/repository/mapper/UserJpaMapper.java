package com.eduardocastro.ecom_application.infrastructure.repository.mapper;

import com.eduardocastro.ecom_application.domain.model.User;
import com.eduardocastro.ecom_application.infrastructure.repository.jpa.UserJpaEntity;

public class UserJpaMapper {

    private UserJpaMapper() {}

    public static UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public static User toDomain(UserJpaEntity entity) {
        return User.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName()
        );
    }
}
