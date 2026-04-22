package com.eduardocastro.user_service.infrastructure.web.mapper;

import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.valueobject.Address;
import com.eduardocastro.user_service.infrastructure.web.dto.request.CreateUserRequest;
import com.eduardocastro.user_service.infrastructure.web.dto.response.UserResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserWebMapperTest {

    private static final Address DOMAIN_ADDRESS = new Address("Rua das Flores", "123", "Apto 1", "Centro", "São Paulo", "SP", "01310100");

    @Test
    void shouldMapAddressRequestToDomainAddress() {
        CreateUserRequest.AddressRequest request = new CreateUserRequest.AddressRequest(
                "Rua das Flores", "123", "Apto 1", "Centro", "São Paulo", "SP", "01310100"
        );

        Address address = UserWebMapper.toAddress(request);

        assertEquals("Rua das Flores", address.getStreet());
        assertEquals("123", address.getNumber());
        assertEquals("Apto 1", address.getComplement());
        assertEquals("Centro", address.getNeighborhood());
        assertEquals("São Paulo", address.getCity());
        assertEquals("SP", address.getState());
        assertEquals("01310100", address.getZipCode());
    }

    @Test
    void shouldMapNullComplementInAddressRequest() {
        CreateUserRequest.AddressRequest request = new CreateUserRequest.AddressRequest(
                "Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100"
        );

        Address address = UserWebMapper.toAddress(request);

        assertNull(address.getComplement());
    }

    @Test
    void shouldMapUserToResponse() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", DOMAIN_ADDRESS, UserRole.USER, now, now);

        UserResponse response = UserWebMapper.toResponse(user);

        assertEquals(id, response.id());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("11987654321", response.phone());
        assertEquals("john@example.com", response.email());
        assertEquals(UserRole.USER, response.role());
        assertEquals(now, response.createdAt());
        assertEquals(now, response.updatedAt());
    }

    @Test
    void shouldMapAddressToAddressResponse() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", DOMAIN_ADDRESS, UserRole.USER, now, now);

        UserResponse.AddressResponse addressResponse = UserWebMapper.toResponse(user).address();

        assertEquals("Rua das Flores", addressResponse.street());
        assertEquals("123", addressResponse.number());
        assertEquals("Apto 1", addressResponse.complement());
        assertEquals("Centro", addressResponse.neighborhood());
        assertEquals("São Paulo", addressResponse.city());
        assertEquals("SP", addressResponse.state());
        assertEquals("01310100", addressResponse.zipCode());
    }

    @Test
    void shouldMapNullComplementToAddressResponse() {
        Address addressWithoutComplement = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", addressWithoutComplement, UserRole.USER, now, now);

        assertNull(UserWebMapper.toResponse(user).address().complement());
    }
}
