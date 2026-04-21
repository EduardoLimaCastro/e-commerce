package com.eduardocastro.user_service.infrastructure.persistence.mapper;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.infrastructure.persistence.jpa.UserJpaEntity;

public class UserJpaMapper {

    private UserJpaMapper() {}

    public static UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static User toDomain(UserJpaEntity entity) {
        return User.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
