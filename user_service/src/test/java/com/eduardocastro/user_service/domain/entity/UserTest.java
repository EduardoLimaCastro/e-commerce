package com.eduardocastro.user_service.domain.entity;

import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
        void shouldSetTimestampsOnCreate() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            User user = User.create("John", "Doe");
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            assertNotNull(user.getCreatedAt());
            assertNotNull(user.getUpdatedAt());
            assertTrue(user.getCreatedAt().isAfter(before) && user.getCreatedAt().isBefore(after));
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
            LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
            LocalDateTime updatedAt = LocalDateTime.now();
            User user = User.reconstitute(id, "John", "Doe", createdAt, updatedAt);

            assertEquals(id, user.getId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
            assertEquals(createdAt, user.getCreatedAt());
            assertEquals(updatedAt, user.getUpdatedAt());
        }

        @Test
        void shouldThrowWhenFirstNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), null, "Doe", LocalDateTime.now(), LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenLastNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", null, LocalDateTime.now(), LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenIdIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(null, "John", "Doe", LocalDateTime.now(), LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenCreatedAtIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", "Doe", null, LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenUpdatedAtIsBeforeCreatedAt() {
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime updatedAt = createdAt.minusSeconds(1);

            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", "Doe", createdAt, updatedAt));
        }

        @Test
        void shouldAllowNullUpdatedAt() {
            assertDoesNotThrow(() ->
                    User.reconstitute(UUID.randomUUID(), "John", "Doe", LocalDateTime.now(), null));
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
        void shouldUpdateUpdatedAtTimestamp() throws InterruptedException {
            User user = User.create("John", "Doe");
            LocalDateTime before = user.getUpdatedAt();

            Thread.sleep(10);
            user.update("Jane", "Smith");

            assertTrue(user.getUpdatedAt().isAfter(before));
        }

        @Test
        void shouldBeNoOpWhenNamesAreUnchanged() {
            User user = User.create("John", "Doe");
            LocalDateTime updatedAt = user.getUpdatedAt();

            user.update("John", "Doe");

            assertEquals(updatedAt, user.getUpdatedAt());
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
            LocalDateTime now = LocalDateTime.now();
            User user1 = User.reconstitute(id, "John", "Doe", now, now);
            User user2 = User.reconstitute(id, "Jane", "Smith", now, now);

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
