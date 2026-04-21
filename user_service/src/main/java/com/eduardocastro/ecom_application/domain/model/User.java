package com.eduardocastro.ecom_application.domain.model;

import com.eduardocastro.ecom_application.domain.exception.InvalidUserDataException;

import java.util.UUID;

public class User {
    private final UUID id;
    private String firstName;
    private String lastName;

    private User(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static User create(String firstName, String lastName) {
        validate(firstName, lastName);
        return new User(UUID.randomUUID(), firstName, lastName);
    }

    public static User reconstitute(UUID id, String firstName, String lastName) {
        validate(firstName, lastName);
        return new User(id, firstName, lastName);
    }

    public void update(String firstName, String lastName) {
        validate(firstName, lastName);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    private static void validate(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new InvalidUserDataException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new InvalidUserDataException("Last name cannot be null or blank");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
