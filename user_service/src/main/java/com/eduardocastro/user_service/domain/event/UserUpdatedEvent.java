package com.eduardocastro.user_service.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserUpdatedEvent(
        UUID aggregateId,
        String firstName,
        String lastName,
        LocalDateTime occurredAt
) implements DomainEvent {}
