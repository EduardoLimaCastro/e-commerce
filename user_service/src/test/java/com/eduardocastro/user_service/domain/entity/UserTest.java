package com.eduardocastro.user_service.domain.entity;

import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.event.UserCreatedEvent;
import com.eduardocastro.user_service.domain.event.UserUpdatedEvent;
import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final String PHONE = "11987654321";
    private static final String EMAIL = "john@example.com";
    private static final UserRole ROLE = UserRole.USER;

    // =========================
    // User.create()
    // =========================

    @Nested
    class Create {

        @Test
        void shouldCreateUserWithValidData() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);

            assertNotNull(user.getId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
            assertEquals(PHONE, user.getPhone().getValue());
            assertEquals(EMAIL, user.getEmail().getValue());
            assertEquals(ROLE, user.getRole());
        }

        @Test
        void shouldSetTimestampsOnCreate() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            assertNotNull(user.getCreatedAt());
            assertNotNull(user.getUpdatedAt());
            assertTrue(user.getCreatedAt().isAfter(before) && user.getCreatedAt().isBefore(after));
        }

        @Test
        void shouldNormalizeEmailToLowercase() {
            User user = User.create("John", "Doe", PHONE, "JOHN@EXAMPLE.COM", ROLE);

            assertEquals("john@example.com", user.getEmail().getValue());
        }

        @Test
        void shouldEmitUserCreatedEvent() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);

            var events = user.pullDomainEvents();

            assertEquals(1, events.size());
            assertInstanceOf(UserCreatedEvent.class, events.getFirst());
            UserCreatedEvent event = (UserCreatedEvent) events.getFirst();
            assertEquals(user.getId(), event.aggregateId());
            assertEquals(PHONE, event.phone());
            assertEquals(EMAIL, event.email());
            assertEquals(ROLE, event.role());
        }

        @Test
        void shouldClearEventsAfterPull() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);
            user.pullDomainEvents();

            assertTrue(user.pullDomainEvents().isEmpty());
        }

        @Test
        void shouldThrowWhenFirstNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create(null, "Doe", PHONE, EMAIL, ROLE));
        }

        @Test
        void shouldThrowWhenFirstNameIsBlank() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("  ", "Doe", PHONE, EMAIL, ROLE));
        }

        @Test
        void shouldThrowWhenLastNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", null, PHONE, EMAIL, ROLE));
        }

        @Test
        void shouldThrowWhenLastNameIsBlank() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", "  ", PHONE, EMAIL, ROLE));
        }

        @Test
        void shouldThrowWhenPhoneIsInvalid() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", "Doe", "123", EMAIL, ROLE));
        }

        @Test
        void shouldThrowWhenEmailIsInvalid() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", "Doe", PHONE, "not-an-email", ROLE));
        }

        @Test
        void shouldGenerateUniqueIds() {
            User user1 = User.create("John", "Doe", PHONE, EMAIL, ROLE);
            User user2 = User.create("John", "Doe", "11911112222", "other@example.com", ROLE);

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
            User user = User.reconstitute(id, "John", "Doe", PHONE, EMAIL, ROLE, createdAt, updatedAt);

            assertEquals(id, user.getId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
            assertEquals(PHONE, user.getPhone().getValue());
            assertEquals(EMAIL, user.getEmail().getValue());
            assertEquals(ROLE, user.getRole());
            assertEquals(createdAt, user.getCreatedAt());
            assertEquals(updatedAt, user.getUpdatedAt());
        }

        @Test
        void shouldNotEmitEventsOnReconstitute() {
            User user = User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ROLE, LocalDateTime.now(), LocalDateTime.now());

            assertTrue(user.pullDomainEvents().isEmpty());
        }

        @Test
        void shouldThrowWhenFirstNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), null, "Doe", PHONE, EMAIL, ROLE, LocalDateTime.now(), LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenLastNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", null, PHONE, EMAIL, ROLE, LocalDateTime.now(), LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenIdIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(null, "John", "Doe", PHONE, EMAIL, ROLE, LocalDateTime.now(), LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenCreatedAtIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ROLE, null, LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenUpdatedAtIsBeforeCreatedAt() {
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime updatedAt = createdAt.minusSeconds(1);

            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ROLE, createdAt, updatedAt));
        }

        @Test
        void shouldAllowNullUpdatedAt() {
            assertDoesNotThrow(() ->
                    User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ROLE, LocalDateTime.now(), null));
        }
    }

    // =========================
    // update()
    // =========================

    @Nested
    class Update {

        @Test
        void shouldUpdateNames() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);

            user.update("Jane", "Smith");

            assertEquals("Jane", user.getFirstName());
            assertEquals("Smith", user.getLastName());
        }

        @Test
        void shouldEmitUserUpdatedEvent() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);
            user.pullDomainEvents();

            user.update("Jane", "Smith");

            var events = user.pullDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(UserUpdatedEvent.class, events.getFirst());
            UserUpdatedEvent event = (UserUpdatedEvent) events.getFirst();
            assertEquals(user.getId(), event.aggregateId());
            assertEquals("Jane", event.firstName());
        }

        @Test
        void shouldUpdateUpdatedAtTimestamp() throws InterruptedException {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);
            LocalDateTime before = user.getUpdatedAt();

            Thread.sleep(10);
            user.update("Jane", "Smith");

            assertTrue(user.getUpdatedAt().isAfter(before));
        }

        @Test
        void shouldBeNoOpWhenNamesAreUnchanged() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);
            user.pullDomainEvents();
            LocalDateTime updatedAt = user.getUpdatedAt();

            user.update("John", "Doe");

            assertEquals(updatedAt, user.getUpdatedAt());
            assertTrue(user.pullDomainEvents().isEmpty());
        }

        @Test
        void shouldThrowWhenFirstNameIsBlankOnUpdate() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);

            assertThrows(InvalidUserDataException.class, () -> user.update("", "Doe"));
        }

        @Test
        void shouldThrowWhenLastNameIsBlankOnUpdate() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ROLE);

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
            User user1 = User.reconstitute(id, "John", "Doe", PHONE, EMAIL, ROLE, now, now);
            User user2 = User.reconstitute(id, "Jane", "Smith", "11911112222", "jane@example.com", UserRole.ADMIN, now, now);

            assertEquals(user1, user2);
            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        void shouldNotBeEqualWhenDifferentIds() {
            User user1 = User.create("John", "Doe", PHONE, EMAIL, ROLE);
            User user2 = User.create("John", "Doe", "11911112222", "other@example.com", ROLE);

            assertNotEquals(user1, user2);
        }
    }
}
