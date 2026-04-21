package com.eduardocastro.ecom_application.application.usecase;

import java.util.UUID;

public interface UpdateUserUseCase {
    boolean execute(UUID id, String firstName, String lastName);
}
