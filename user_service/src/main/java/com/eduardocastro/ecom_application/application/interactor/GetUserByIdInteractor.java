package com.eduardocastro.ecom_application.application.interactor;

import com.eduardocastro.ecom_application.application.usecase.GetUserByIdUseCase;
import com.eduardocastro.ecom_application.domain.entity.User;
import com.eduardocastro.ecom_application.domain.repository.UserRepository;
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
