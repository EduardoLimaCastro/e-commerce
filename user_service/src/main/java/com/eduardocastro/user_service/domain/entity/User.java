package com.eduardocastro.user_service.domain.entity;

import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.event.DomainEvent;
import com.eduardocastro.user_service.domain.event.UserCreatedEvent;
import com.eduardocastro.user_service.domain.event.UserUpdatedEvent;
import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;
import com.eduardocastro.user_service.domain.valueobject.Email;
import com.eduardocastro.user_service.domain.valueobject.Phone;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class User {

    private final UUID id;
    private String firstName;
    private String lastName;
    private Email email;
    private Phone phone;
    private UserRole role;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private User(UUID id, String firstName, String lastName, Email email, Phone phone ,UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String firstName, String lastName,String phone ,String email, UserRole role) {
        validate(firstName, lastName);
        Email emailVO = new Email(email);
        Phone phoneVO = new Phone(phone);
        LocalDateTime now = LocalDateTime.now();
        User user = new User(UUID.randomUUID(), firstName, lastName, emailVO, phoneVO,role, now, now);
        user.domainEvents.add(new UserCreatedEvent(user.id, firstName, lastName, emailVO.getValue(), phoneVO.getValue(), role, now));
        return user;
    }

    public static User reconstitute(UUID id, String firstName, String lastName, String phone, String email, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        validate(firstName, lastName);
        validateReconstitution(id, createdAt, updatedAt);
        return new User(id, firstName, lastName, new Email(email), new Phone(phone), role, createdAt, updatedAt);
    }

    public void update(String firstName, String lastName) {
        validate(firstName, lastName);
        if (this.firstName.equals(firstName) && this.lastName.equals(lastName)) return;
        this.firstName = firstName;
        this.lastName = lastName;
        touch();
        domainEvents.add(new UserUpdatedEvent(id, firstName, lastName, this.updatedAt));
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = Collections.unmodifiableList(new ArrayList<>(domainEvents));
        domainEvents.clear();
        return events;
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
    public int hashCode() { return id.hashCode(); }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public Email getEmail() { return email; }
    public Phone getPhone() { return phone; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
