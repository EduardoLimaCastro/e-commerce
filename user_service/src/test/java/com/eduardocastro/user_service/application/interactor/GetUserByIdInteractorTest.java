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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserByIdInteractorTest {

    @Mock
    private UserRepository userRepository;

    private GetUserByIdInteractor interactor;

    private static final Address ADDRESS = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");

    @BeforeEach
    void setUp() {
        interactor = new GetUserByIdInteractor(userRepository);
    }

    @Test
    void shouldReturnUserWhenFound() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = interactor.execute(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = interactor.execute(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldDelegateToRepositoryWithCorrectId() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        interactor.execute(id);

        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }
}
