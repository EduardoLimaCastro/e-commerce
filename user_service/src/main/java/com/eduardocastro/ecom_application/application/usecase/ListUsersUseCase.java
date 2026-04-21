package com.eduardocastro.ecom_application.application.usecase;

import com.eduardocastro.ecom_application.domain.entity.User;

import java.util.List;

public interface ListUsersUseCase {
    List<User> execute();
}
