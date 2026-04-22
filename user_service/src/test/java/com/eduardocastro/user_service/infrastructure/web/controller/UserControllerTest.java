package com.eduardocastro.user_service.infrastructure.web.controller;

import com.eduardocastro.user_service.application.usecase.CreateUserUseCase;
import com.eduardocastro.user_service.application.usecase.GetUserByIdUseCase;
import com.eduardocastro.user_service.application.usecase.ListUsersUseCase;
import com.eduardocastro.user_service.application.usecase.UpdateUserUseCase;
import com.eduardocastro.user_service.domain.entity.User;
import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.valueobject.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

    @MockitoBean
    private ListUsersUseCase listUsersUseCase;

    @MockitoBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @MockitoBean
    private UpdateUserUseCase updateUserUseCase;

    private static final Address ADDRESS = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");

    private static final String VALID_REQUEST_BODY = """
            {
                "firstName": "John",
                "lastName": "Doe",
                "phone": "11987654321",
                "email": "john@example.com",
                "address": {
                    "street": "Rua das Flores",
                    "number": "123",
                    "neighborhood": "Centro",
                    "city": "São Paulo",
                    "state": "SP",
                    "zipCode": "01310100"
                },
                "role": "USER"
            }
            """;

    @Test
    void shouldCreateUserAndReturn201() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        when(createUserUseCase.execute(any(), any(), any(), any(), any(), any())).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldListUsersAndReturn200() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(UUID.randomUUID(), "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        when(listUsersUseCase.execute()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void shouldReturnEmptyListWhenNoUsers() throws Exception {
        when(listUsersUseCase.execute()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void shouldGetUserByIdAndReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        User user = User.reconstitute(id, "John", "Doe", "11987654321", "john@example.com", ADDRESS, UserRole.USER, now, now);
        when(getUserByIdUseCase.execute(id)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(getUserByIdUseCase.execute(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateUserAndReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        when(updateUserUseCase.execute(eq(id), any(), any(), any(), any(), any(), any())).thenReturn(true);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST_BODY))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentUser() throws Exception {
        UUID id = UUID.randomUUID();
        when(updateUserUseCase.execute(eq(id), any(), any(), any(), any(), any(), any())).thenReturn(false);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_REQUEST_BODY))
                .andExpect(status().isNotFound());
    }
}
