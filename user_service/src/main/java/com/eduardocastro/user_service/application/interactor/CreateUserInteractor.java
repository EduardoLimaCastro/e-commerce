package com.eduardocastro.user_service.application.interactor;

import com.eduardocastro.user_service.application.usecase.CreateUserUseCase;
import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserInteractor implements CreateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public User execute(String firstName, String lastName) {
        return userRepository.save(User.create(firstName, lastName));
    }
}
