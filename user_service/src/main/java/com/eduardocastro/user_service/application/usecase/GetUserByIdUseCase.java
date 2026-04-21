package com.eduardocastro.user_service.application.usecase;

import com.eduardocastro.user_service.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface GetUserByIdUseCase {
    Optional<User> execute(UUID id);
}
