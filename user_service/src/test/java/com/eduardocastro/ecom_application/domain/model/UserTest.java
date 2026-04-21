package com.eduardocastro.ecom_application.domain.model;

import com.eduardocastro.ecom_application.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    // =========================
    // User.create()
    // =========================

    @Nested
    class Create {

        @Test
        void shouldCreateUserWithValidData() {
            User user = User.create("John", "Doe");

            assertNotNull(user.getId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
        }

        @Test
        void shouldThrowWhenFirstNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create(null, "Doe"));
        }

        @Test
        void shouldThrowWhenFirstNameIsBlank() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("  ", "Doe"));
        }

        @Test
        void shouldThrowWhenLastNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", null));
        }

        @Test
        void shouldThrowWhenLastNameIsBlank() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", "  "));
        }

        @Test
        void shouldGenerateUniqueIds() {
            User user1 = User.create("John", "Doe");
            User user2 = User.create("John", "Doe");

            assertNotEquals(user1.getId(), user2.getId());
        }
    }

    // =========================
    // User.reconstitute()
    // =========================

    @Nested
    class Reconstitute {

        @Test
        void shouldReconstituteUserWithGivenId() {
            UUID id = UUID.randomUUID();
            User user = User.reconstitute(id, "John", "Doe");

            assertEquals(id, user.getId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
        }

        @Test
        void shouldThrowWhenFirstNameIsInvalid() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), null, "Doe"));
        }
    }

    // =========================
    // update()
    // =========================

    @Nested
    class Update {

        @Test
        void shouldUpdateNames() {
            User user = User.create("John", "Doe");

            user.update("Jane", "Smith");

            assertEquals("Jane", user.getFirstName());
            assertEquals("Smith", user.getLastName());
        }

        @Test
        void shouldThrowWhenFirstNameIsBlankOnUpdate() {
            User user = User.create("John", "Doe");

            assertThrows(InvalidUserDataException.class, () -> user.update("", "Doe"));
        }

        @Test
        void shouldThrowWhenLastNameIsBlankOnUpdate() {
            User user = User.create("John", "Doe");

            assertThrows(InvalidUserDataException.class, () -> user.update("John", "   "));
        }
    }

    // =========================
    // Identity
    // =========================

    @Nested
    class Identity {

        @Test
        void shouldBeEqualWhenSameId() {
            UUID id = UUID.randomUUID();
            User user1 = User.reconstitute(id, "John", "Doe");
            User user2 = User.reconstitute(id, "Jane", "Smith");

            assertEquals(user1, user2);
            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        void shouldNotBeEqualWhenDifferentIds() {
            User user1 = User.create("John", "Doe");
            User user2 = User.create("John", "Doe");

            assertNotEquals(user1, user2);
        }
    }
}
