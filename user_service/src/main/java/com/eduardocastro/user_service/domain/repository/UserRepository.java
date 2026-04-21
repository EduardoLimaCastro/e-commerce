package com.eduardocastro.user_service.domain.repository;

import com.eduardocastro.user_service.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    User save(User user);
    Optional<User> findById(UUID id);
}
