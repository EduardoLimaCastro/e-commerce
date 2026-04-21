package com.eduardocastro.user_service.infrastructure.persistence.adapter;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.repository.UserRepository;
import com.eduardocastro.user_service.infrastructure.persistence.jpa.UserJpaRepository;
import com.eduardocastro.user_service.infrastructure.persistence.mapper.UserJpaMapper;
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
