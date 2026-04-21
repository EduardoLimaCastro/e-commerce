package com.eduardocastro.user_service.application.interactor;

import com.eduardocastro.user_service.application.usecase.GetUserByIdUseCase;
import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByIdInteractor implements GetUserByIdUseCase {

    private final UserRepository userRepository;

    @Override
    public Optional<User> execute(UUID id) {
        return userRepository.findById(id);
    }
}
