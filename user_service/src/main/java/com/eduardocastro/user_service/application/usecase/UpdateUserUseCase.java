package com.eduardocastro.user_service.application.usecase;

import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.valueobject.Address;

import java.util.UUID;

public interface UpdateUserUseCase {
    boolean execute(UUID id, String firstName, String lastName, String phone, String email, Address address, UserRole role);
}
