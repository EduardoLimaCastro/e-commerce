package com.eduardocastro.user_service.application.interactor;

import com.eduardocastro.user_service.application.usecase.UpdateUserUseCase;
import com.eduardocastro.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserInteractor implements UpdateUserUseCase {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean execute(UUID id, String firstName, String lastName) {
        return userRepository.findById(id)
                .map(user -> {
                    user.update(firstName, lastName);
                    userRepository.save(user);
                    user.pullDomainEvents().forEach(eventPublisher::publishEvent);
                    return true;
                })
                .orElse(false);
    }
}
