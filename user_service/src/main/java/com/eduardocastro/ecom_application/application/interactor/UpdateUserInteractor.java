package com.eduardocastro.ecom_application.application.interactor;

import com.eduardocastro.ecom_application.application.usecase.UpdateUserUseCase;
import com.eduardocastro.ecom_application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserInteractor implements UpdateUserUseCase {

    private final UserRepository userRepository;

    @Override
    public boolean execute(UUID id, String firstName, String lastName) {
        return userRepository.findById(id)
                .map(user -> {
                    user.update(firstName, lastName);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }
}
