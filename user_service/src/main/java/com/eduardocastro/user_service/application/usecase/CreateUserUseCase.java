package com.eduardocastro.user_service.application.usecase;

import com.eduardocastro.user_service.domain.entity.User;

public interface CreateUserUseCase {
    User execute(String firstName, String lastName);
}
