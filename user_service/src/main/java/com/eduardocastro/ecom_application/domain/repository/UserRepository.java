package com.eduardocastro.ecom_application.domain.repository;

import com.eduardocastro.ecom_application.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    User save(User user);
    Optional<User> findById(UUID id);
}
