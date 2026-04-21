package com.eduardocastro.ecom_application.application.usecase;

import com.eduardocastro.ecom_application.domain.entity.User;

public interface CreateUserUseCase {
    User execute(String firstName, String lastName);
}
