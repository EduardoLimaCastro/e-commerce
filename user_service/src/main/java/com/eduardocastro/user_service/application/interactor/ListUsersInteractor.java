package com.eduardocastro.user_service.application.interactor;

import com.eduardocastro.user_service.application.usecase.ListUsersUseCase;
import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.repository.UserRepository;
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
