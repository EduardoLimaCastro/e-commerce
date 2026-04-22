package com.eduardocastro.user_service.application.interactor;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.event.DomainEvent;
import com.eduardocastro.user_service.domain.repository.UserRepository;
import com.eduardocastro.user_service.domain.valueobject.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserInteractorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private UpdateUserInteractor interactor;

    private static final Address ADDRESS = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");
    private static final Address NEW_ADDRESS = new Address("Av. Paulista", "1000", null, "Bela Vista", "São Paulo", "SP", "01310100");

    @BeforeEach
    void setUp() {
        interactor = new UpdateUserInteractor(userRepository, eventPublisher);
    }

    @Test
    void shouldReturnTrueAndSaveWhenUserFound() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        boolean result = interactor.execute(id, "Jane", "Smith", "11911112222", "jane@example.com", NEW_ADDRESS, UserRole.ADMIN);

        assertTrue(result);
        verify(userRepository).save(user);
    }

    @Test
    void shouldReturnFalseWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = interactor.execute(id, "Jane", "Smith", "11911112222", "jane@example.com", NEW_ADDRESS, UserRole.ADMIN);

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldPublishEventsAfterUpdate() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        interactor.execute(id, "Jane", "Smith", "11911112222", "jane@example.com", NEW_ADDRESS, UserRole.ADMIN);

        verify(eventPublisher, atLeastOnce()).publishEvent(any(DomainEvent.class));
    }

    @Test
    void shouldNotPublishEventsWhenUserNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        interactor.execute(id, "Jane", "Smith", "11911112222", "jane@example.com", NEW_ADDRESS, UserRole.ADMIN);

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void shouldNotPublishEventsWhenNothingChanged() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        interactor.execute(id, "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);

        verify(eventPublisher, never()).publishEvent(any());
    }
}
