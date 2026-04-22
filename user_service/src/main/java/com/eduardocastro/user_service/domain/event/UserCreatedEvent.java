package com.eduardocastro.user_service.domain.event;

import com.eduardocastro.user_service.domain.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserCreatedEvent(
        UUID aggregateId,
        String firstName,
        String lastName,
        String email,
        String  phone,
        UserRole role,
        LocalDateTime occurredAt
) implements DomainEvent {}
