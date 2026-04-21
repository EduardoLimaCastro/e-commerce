package com.eduardocastro.user_service.infrastructure.web.controller;

import com.eduardocastro.user_service.application.usecase.CreateUserUseCase;
import com.eduardocastro.user_service.application.usecase.GetUserByIdUseCase;
import com.eduardocastro.user_service.application.usecase.ListUsersUseCase;
import com.eduardocastro.user_service.application.usecase.UpdateUserUseCase;
import com.eduardocastro.user_service.infrastructure.web.dto.request.CreateUserRequest;
import com.eduardocastro.user_service.infrastructure.web.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(createUserUseCase.execute(request.firstName(), request.lastName())));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> list() {
        return ResponseEntity.ok(listUsersUseCase.execute().stream().map(UserResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return getUserByIdUseCase.execute(id)
                .map(UserResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody CreateUserRequest request) {
        boolean updated = updateUserUseCase.execute(id, request.firstName(), request.lastName());
        return updated ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
