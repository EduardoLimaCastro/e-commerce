package com.eduardocastro.user_service.domain.event;

import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.valueobject.Address;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCreatedEvent(
        UUID aggregateId,
        String firstName,
        String lastName,
        String email,
        String phone,
        Address address,
        UserRole role,
        LocalDateTime occurredAt
) implements DomainEvent {}
