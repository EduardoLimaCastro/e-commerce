package com.eduardocastro.ecom_application.application.interactor;

import com.eduardocastro.ecom_application.application.usecase.CreateUserUseCase;
import com.eduardocastro.ecom_application.domain.entity.User;
import com.eduardocastro.ecom_application.domain.repository.UserRepository;
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
