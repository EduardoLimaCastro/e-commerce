package com.eduardocastro.ecom_application.application.interactor;

import com.eduardocastro.ecom_application.application.usecase.ListUsersUseCase;
import com.eduardocastro.ecom_application.domain.entity.User;
import com.eduardocastro.ecom_application.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersInteractor implements ListUsersUseCase {

    private final UserRepository userRepository;

    @Override
    public List<User> execute() {
        return userRepository.findAll();
    }
}
