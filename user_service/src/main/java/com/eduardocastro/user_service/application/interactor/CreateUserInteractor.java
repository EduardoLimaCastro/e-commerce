package com.eduardocastro.user_service.application.interactor;

import com.eduardocastro.user_service.application.usecase.CreateUserUseCase;
import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserInteractor implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public User execute(String firstName, String lastName, String phone, String email, UserRole role) {
        User user = User.create(firstName, lastName, phone, email, role);
        User saved = userRepository.save(user);
        user.pullDomainEvents().forEach(eventPublisher::publishEvent);
        return saved;
    }
}
