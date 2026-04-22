package com.eduardocastro.user_service.infrastructure.persistence.mapper;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.valueobject.Address;
import com.eduardocastro.user_service.infrastructure.persistence.jpa.AddressEmbeddable;
import com.eduardocastro.user_service.infrastructure.persistence.jpa.UserJpaEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserJpaMapperTest {

    private static final Address DOMAIN_ADDRESS = new Address("Rua das Flores", "123", "Apto 1", "Centro", "São Paulo", "SP", "01310100");

    @Test
    void shouldMapDomainUserToJpaEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", DOMAIN_ADDRESS, UserRole.USER, now, now);

        UserJpaEntity entity = UserJpaMapper.toEntity(user);

        assertEquals(id, entity.getId());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("11987654321", entity.getPhone());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals(UserRole.USER, entity.getRole());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void shouldMapAddressToEmbeddable() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", DOMAIN_ADDRESS, UserRole.USER, now, now);

        AddressEmbeddable embeddable = UserJpaMapper.toEntity(user).getAddress();

        assertEquals("Rua das Flores", embeddable.getStreet());
        assertEquals("123", embeddable.getNumber());
        assertEquals("Apto 1", embeddable.getComplement());
        assertEquals("Centro", embeddable.getNeighborhood());
        assertEquals("São Paulo", embeddable.getCity());
        assertEquals("SP", embeddable.getState());
        assertEquals("01310100", embeddable.getZipCode());
    }

    @Test
    void shouldMapJpaEntityToDomainUser() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        AddressEmbeddable embeddable = new AddressEmbeddable("Rua das Flores", "123", "Apto 1", "Centro", "São Paulo", "SP", "01310100");
        UserJpaEntity entity = new UserJpaEntity(id, "John", "Doe", "11987654321", "john@example.com", embeddable, UserRole.USER, now, now);

        User user = UserJpaMapper.toDomain(entity);

        assertEquals(id, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("11987654321", user.getPhone().getValue());
        assertEquals("john@example.com", user.getEmail().getValue());
        assertEquals(UserRole.USER, user.getRole());
    }

    @Test
    void shouldMapAddressEmbeddableToDomainAddress() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        AddressEmbeddable embeddable = new AddressEmbeddable("Rua das Flores", "123", "Apto 1", "Centro", "São Paulo", "SP", "01310100");
        UserJpaEntity entity = new UserJpaEntity(id, "John", "Doe", "11987654321", "john@example.com", embeddable, UserRole.USER, now, now);

        Address address = UserJpaMapper.toDomain(entity).getAddress();

        assertEquals("Rua das Flores", address.getStreet());
        assertEquals("123", address.getNumber());
        assertEquals("Apto 1", address.getComplement());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("SP", address.getState());
        assertEquals("01310100", address.getZipCode());
    }

    @Test
    void shouldRoundTripUserThroughJpaMapping() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User original = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", DOMAIN_ADDRESS, UserRole.USER, now, now);

        User restored = UserJpaMapper.toDomain(UserJpaMapper.toEntity(original));

        assertEquals(original.getId(), restored.getId());
        assertEquals(original.getFirstName(), restored.getFirstName());
        assertEquals(original.getLastName(), restored.getLastName());
        assertEquals(original.getEmail().getValue(), restored.getEmail().getValue());
        assertEquals(original.getPhone().getValue(), restored.getPhone().getValue());
        assertEquals(original.getAddress(), restored.getAddress());
        assertEquals(original.getRole(), restored.getRole());
    }
}
