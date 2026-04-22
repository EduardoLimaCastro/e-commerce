package com.eduardocastro.user_service.application.interactor;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.repository.UserRepository;
import com.eduardocastro.user_service.domain.valueobject.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUsersInteractorTest {

    @Mock
    private UserRepository userRepository;

    private ListUsersInteractor interactor;

    private static final Address ADDRESS = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");

    @BeforeEach
    void setUp() {
        interactor = new ListUsersInteractor(userRepository);
    }

    @Test
    void shouldReturnAllUsers() {
        LocalDateTime now = LocalDateTime.now();
        User user1 = User.reconstitute(UUID.randomUUID(), "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        User user2 = User.reconstitute(UUID.randomUUID(), "Jane", "Smith", "11911112222", "jane@example.com", ADDRESS, UserRole.ADMIN, now, now);
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = interactor.execute();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = interactor.execute();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDelegateToRepositoryFindAll() {
        when(userRepository.findAll()).thenReturn(List.of());

        interactor.execute();

        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }
}
