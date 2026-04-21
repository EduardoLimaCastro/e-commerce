package com.eduardocastro.user_service.application.usecase;

import com.eduardocastro.user_service.domain.entity.User;

import java.util.List;

public interface ListUsersUseCase {
    List<User> execute();
}
