package com.eduardocastro.user_service.domain.valueobject;

import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;

import java.util.Objects;

public final class Email {

    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank())
            throw new InvalidUserDataException("Email cannot be null or blank");
        if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new InvalidUserDataException("Invalid email format: " + value);
        this.value = value.toLowerCase();
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }
}
