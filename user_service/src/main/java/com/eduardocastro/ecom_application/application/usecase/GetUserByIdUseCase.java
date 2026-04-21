package com.eduardocastro.ecom_application.application.usecase;

import com.eduardocastro.ecom_application.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface GetUserByIdUseCase {
    Optional<User> execute(UUID id);
}
