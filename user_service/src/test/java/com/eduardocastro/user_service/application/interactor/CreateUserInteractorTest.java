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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserInteractorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private CreateUserInteractor interactor;

    private static final Address ADDRESS = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");

    @BeforeEach
    void setUp() {
        interactor = new CreateUserInteractor(userRepository, eventPublisher);
    }

    @Test
    void shouldSaveUserAndReturnSavedUser() {
        User saved = User.create("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = interactor.execute("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldPublishUserCreatedEvent() {
        User saved = User.create("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        interactor.execute("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);

        verify(eventPublisher, atLeastOnce()).publishEvent(any(DomainEvent.class));
    }

    @Test
    void shouldSaveBeforePublishingEvents() {
        User saved = User.create("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        interactor.execute("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);

        var inOrder = inOrder(userRepository, eventPublisher);
        inOrder.verify(userRepository).save(any(User.class));
        inOrder.verify(eventPublisher).publishEvent(any(DomainEvent.class));
    }

    @Test
    void shouldReturnSavedUserNotOriginal() {
        User returned = User.create("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);
        when(userRepository.save(any(User.class))).thenReturn(returned);

        User result = interactor.execute("John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER);

        assertSame(returned, result);
    }
}
