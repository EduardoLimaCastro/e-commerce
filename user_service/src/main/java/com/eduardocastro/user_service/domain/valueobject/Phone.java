package com.eduardocastro.user_service.domain.valueobject;

import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;

import java.util.Objects;

public final class Phone {

    private final String value;

    public Phone(String value) {
        if (value == null || value.isBlank())
            throw new InvalidUserDataException("Phone cannot be null or blank");
        if (!value.matches("^\\d{11}$"))
            throw new InvalidUserDataException("Invalid phone format, expected 11 digits (DDD + number): " + value);
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phone other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() { return Objects.hash(value); }

    @Override
    public String toString() { return value; }
}
