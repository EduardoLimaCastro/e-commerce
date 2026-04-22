package com.eduardocastro.user_service.application.usecase;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;

public interface CreateUserUseCase {
    User execute(String firstName, String lastName, String phone, String email, UserRole role);
}
