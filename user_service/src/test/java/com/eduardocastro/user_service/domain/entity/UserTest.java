package com.eduardocastro.user_service.domain.entity;

import com.eduardocastro.user_service.domain.enums.UserRole;
import com.eduardocastro.user_service.domain.event.UserCreatedEvent;
import com.eduardocastro.user_service.domain.event.UserUpdatedEvent;
import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;
import com.eduardocastro.user_service.domain.valueobject.Address;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final String PHONE = "11987654321";
    private static final String EMAIL = "john@example.com";
    private static final UserRole ROLE = UserRole.USER;
    private static final Address ADDRESS = new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");

    // =========================
    // User.create()
    // =========================

    @Nested
    class Create {

        @Test
        void shouldCreateUserWithValidData() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);

            assertNotNull(user.getId());
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
            assertEquals(PHONE, user.getPhone().getValue());
            assertEquals(EMAIL, user.getEmail().getValue());
            assertEquals(ADDRESS, user.getAddress());
            assertEquals(ROLE, user.getRole());
        }

        @Test
        void shouldSetTimestampsOnCreate() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            LocalDateTime after = LocalDateTime.now().plusSeconds(1);

            assertTrue(user.getCreatedAt().isAfter(before) && user.getCreatedAt().isBefore(after));
        }

        @Test
        void shouldEmitUserCreatedEvent() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);

            var events = user.pullDomainEvents();

            assertEquals(1, events.size());
            assertInstanceOf(UserCreatedEvent.class, events.getFirst());
            UserCreatedEvent event = (UserCreatedEvent) events.getFirst();
            assertEquals(user.getId(), event.aggregateId());
            assertEquals(EMAIL, event.email());
            assertEquals(PHONE, event.phone());
            assertEquals(ADDRESS, event.address());
            assertEquals(ROLE, event.role());
        }

        @Test
        void shouldClearEventsAfterPull() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            user.pullDomainEvents();

            assertTrue(user.pullDomainEvents().isEmpty());
        }

        @Test
        void shouldThrowWhenFirstNameIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create(null, "Doe", PHONE, EMAIL, ADDRESS, ROLE));
        }

        @Test
        void shouldThrowWhenLastNameIsBlank() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", "  ", PHONE, EMAIL, ADDRESS, ROLE));
        }

        @Test
        void shouldThrowWhenPhoneIsInvalid() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", "Doe", "123", EMAIL, ADDRESS, ROLE));
        }

        @Test
        void shouldThrowWhenEmailIsInvalid() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.create("John", "Doe", PHONE, "not-an-email", ADDRESS, ROLE));
        }

        @Test
        void shouldGenerateUniqueIds() {
            User user1 = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            User user2 = User.create("John", "Doe", "11911112222", "other@example.com", ADDRESS, ROLE);

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
            User user = User.reconstitute(id, "John", "Doe", PHONE, EMAIL, ADDRESS, ROLE, createdAt, updatedAt);

            assertEquals(id, user.getId());
            assertEquals(PHONE, user.getPhone().getValue());
            assertEquals(EMAIL, user.getEmail().getValue());
            assertEquals(ADDRESS, user.getAddress());
            assertEquals(ROLE, user.getRole());
        }

        @Test
        void shouldNotEmitEventsOnReconstitute() {
            User user = User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ADDRESS, ROLE, LocalDateTime.now(), LocalDateTime.now());

            assertTrue(user.pullDomainEvents().isEmpty());
        }

        @Test
        void shouldThrowWhenIdIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(null, "John", "Doe", PHONE, EMAIL, ADDRESS, ROLE, LocalDateTime.now(), LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenCreatedAtIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ADDRESS, ROLE, null, LocalDateTime.now()));
        }

        @Test
        void shouldThrowWhenUpdatedAtIsBeforeCreatedAt() {
            LocalDateTime createdAt = LocalDateTime.now();
            assertThrows(InvalidUserDataException.class,
                    () -> User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ADDRESS, ROLE, createdAt, createdAt.minusSeconds(1)));
        }

        @Test
        void shouldAllowNullUpdatedAt() {
            assertDoesNotThrow(() ->
                    User.reconstitute(UUID.randomUUID(), "John", "Doe", PHONE, EMAIL, ADDRESS, ROLE, LocalDateTime.now(), null));
        }
    }

    // =========================
    // update()
    // =========================

    @Nested
    class Update {

        @Test
        void shouldUpdateAllFields() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            Address newAddress = new Address("Av. Paulista", "1000", null, "Bela Vista", "São Paulo", "SP", "01310100");

            user.update("Jane", "Smith", "11911112222", "jane@example.com", newAddress, UserRole.ADMIN);

            assertEquals("Jane", user.getFirstName());
            assertEquals("Smith", user.getLastName());
            assertEquals("11911112222", user.getPhone().getValue());
            assertEquals("jane@example.com", user.getEmail().getValue());
            assertEquals(newAddress, user.getAddress());
            assertEquals(UserRole.ADMIN, user.getRole());
        }

        @Test
        void shouldEmitUserUpdatedEvent() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            user.pullDomainEvents();

            user.update("Jane", "Smith", "11911112222", "jane@example.com", ADDRESS, UserRole.ADMIN);

            var events = user.pullDomainEvents();
            assertEquals(1, events.size());
            assertInstanceOf(UserUpdatedEvent.class, events.getFirst());
            UserUpdatedEvent event = (UserUpdatedEvent) events.getFirst();
            assertEquals(user.getId(), event.aggregateId());
            assertEquals("Jane", event.firstName());
            assertEquals("jane@example.com", event.email());
            assertEquals(UserRole.ADMIN, event.role());
        }

        @Test
        void shouldUpdateUpdatedAtTimestamp() throws InterruptedException {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            LocalDateTime before = user.getUpdatedAt();

            Thread.sleep(10);
            user.update("Jane", "Smith", "11911112222", "jane@example.com", ADDRESS, UserRole.ADMIN);

            assertTrue(user.getUpdatedAt().isAfter(before));
        }

        @Test
        void shouldBeNoOpWhenAllFieldsAreUnchanged() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            user.pullDomainEvents();
            LocalDateTime updatedAt = user.getUpdatedAt();

            user.update("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);

            assertEquals(updatedAt, user.getUpdatedAt());
            assertTrue(user.pullDomainEvents().isEmpty());
        }

        @Test
        void shouldThrowWhenFirstNameIsBlankOnUpdate() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            assertThrows(InvalidUserDataException.class, () -> user.update("", "Doe", PHONE, EMAIL, ADDRESS, ROLE));
        }

        @Test
        void shouldThrowWhenPhoneIsInvalidOnUpdate() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            assertThrows(InvalidUserDataException.class, () -> user.update("John", "Doe", "123", EMAIL, ADDRESS, ROLE));
        }

        @Test
        void shouldThrowWhenEmailIsInvalidOnUpdate() {
            User user = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            assertThrows(InvalidUserDataException.class, () -> user.update("John", "Doe", PHONE, "not-an-email", ADDRESS, ROLE));
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
            User user1 = User.reconstitute(id, "John", "Doe", PHONE, EMAIL, ADDRESS, ROLE, now, now);
            User user2 = User.reconstitute(id, "Jane", "Smith", "11911112222", "jane@example.com", ADDRESS, UserRole.ADMIN, now, now);

            assertEquals(user1, user2);
            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        void shouldNotBeEqualWhenDifferentIds() {
            User user1 = User.create("John", "Doe", PHONE, EMAIL, ADDRESS, ROLE);
            User user2 = User.create("John", "Doe", "11911112222", "other@example.com", ADDRESS, ROLE);

            assertNotEquals(user1, user2);
        }
    }
}
