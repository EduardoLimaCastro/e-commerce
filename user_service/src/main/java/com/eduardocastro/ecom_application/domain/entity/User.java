package com.eduardocastro.ecom_application.domain.entity;

import com.eduardocastro.ecom_application.domain.exception.InvalidUserDataException;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private final UUID id;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User(UUID id, String firstName, String lastName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String firstName, String lastName) {
        validate(firstName, lastName);
        return new User(UUID.randomUUID(), firstName, lastName, LocalDateTime.now(), LocalDateTime.now());
    }

    public static User reconstitute(UUID id, String firstName, String lastName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validate(firstName, lastName);
        validateReconstitution(id, createdAt, updatedAt);
        return new User(id, firstName, lastName, createdAt, updatedAt);
    }

    public void update(String firstName, String lastName) {
        validate(firstName, lastName);
        if (this.firstName.equals(firstName) && this.lastName.equals(lastName)) return;
        this.firstName = firstName;
        this.lastName = lastName;
        touch();
    }

    private static void validate(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank())
            throw new InvalidUserDataException("First name cannot be null or blank");
        if (lastName == null || lastName.isBlank())
            throw new InvalidUserDataException("Last name cannot be null or blank");
    }

    private static void validateReconstitution(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (id == null)
            throw new InvalidUserDataException("Id cannot be null");
        if (createdAt == null)
            throw new InvalidUserDataException("createdAt cannot be null");
        if (updatedAt != null && updatedAt.isBefore(createdAt))
            throw new InvalidUserDataException("updatedAt cannot be before createdAt");
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
