package com.eduardocastro.ecom_application.infrastructure.persistence.adapter;

import com.eduardocastro.ecom_application.domain.entity.User;
import com.eduardocastro.ecom_application.domain.repository.UserRepository;
import com.eduardocastro.ecom_application.infrastructure.persistence.jpa.UserJpaRepository;
import com.eduardocastro.ecom_application.infrastructure.persistence.mapper.UserJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(UserJpaMapper::toDomain)
                .toList();
    }

    @Override
    public User save(User user) {
        return UserJpaMapper.toDomain(jpaRepository.save(UserJpaMapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(UserJpaMapper::toDomain);
    }
}
