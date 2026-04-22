package com.eduardocastro.user_service.domain.valueobject;

import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateWithValidEmail() {
            Email email = new Email("john@example.com");

            assertEquals("john@example.com", email.getValue());
        }

        @Test
        void shouldNormalizeToLowercase() {
            Email email = new Email("JOHN@EXAMPLE.COM");

            assertEquals("john@example.com", email.getValue());
        }

        @Test
        void shouldThrowWhenNull() {
            assertThrows(InvalidUserDataException.class, () -> new Email(null));
        }

        @Test
        void shouldThrowWhenBlank() {
            assertThrows(InvalidUserDataException.class, () -> new Email("  "));
        }

        @Test
        void shouldThrowWhenMissingAtSign() {
            assertThrows(InvalidUserDataException.class, () -> new Email("johnexample.com"));
        }

        @Test
        void shouldThrowWhenMissingDomain() {
            assertThrows(InvalidUserDataException.class, () -> new Email("john@"));
        }

        @Test
        void shouldThrowWhenMissingTld() {
            assertThrows(InvalidUserDataException.class, () -> new Email("john@example"));
        }
    }

    @Nested
    class Equality {

        @Test
        void shouldBeEqualWhenSameValue() {
            Email email1 = new Email("john@example.com");
            Email email2 = new Email("JOHN@EXAMPLE.COM");

            assertEquals(email1, email2);
            assertEquals(email1.hashCode(), email2.hashCode());
        }

        @Test
        void shouldNotBeEqualWhenDifferentValues() {
            Email email1 = new Email("john@example.com");
            Email email2 = new Email("jane@example.com");

            assertNotEquals(email1, email2);
        }
    }
}
