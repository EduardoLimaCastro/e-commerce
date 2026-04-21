package com.eduardocastro.user_service.application.usecase;

import java.util.UUID;

public interface UpdateUserUseCase {
    boolean execute(UUID id, String firstName, String lastName);
}
