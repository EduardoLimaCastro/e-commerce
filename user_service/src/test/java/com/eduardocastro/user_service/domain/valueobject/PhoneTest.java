package com.eduardocastro.user_service.domain.valueobject;

import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneTest {

    @Nested
    class Creation {

        @Test
        void shouldCreateWithValidPhone() {
            Phone phone = new Phone("11987654321");
            assertEquals("11987654321", phone.getValue());
        }

        @Test
        void shouldThrowWhenNull() {
            assertThrows(InvalidUserDataException.class, () -> new Phone(null));
        }

        @Test
        void shouldThrowWhenBlank() {
            assertThrows(InvalidUserDataException.class, () -> new Phone("  "));
        }

        @Test
        void shouldThrowWhenTooShort() {
            assertThrows(InvalidUserDataException.class, () -> new Phone("1198765432"));
        }

        @Test
        void shouldThrowWhenTooLong() {
            assertThrows(InvalidUserDataException.class, () -> new Phone("119876543210"));
        }

        @Test
        void shouldThrowWhenContainsLetters() {
            assertThrows(InvalidUserDataException.class, () -> new Phone("1198765432A"));
        }

        @Test
        void shouldThrowWhenContainsSymbols() {
            assertThrows(InvalidUserDataException.class, () -> new Phone("11987-54321"));
        }
    }

    @Nested
    class Equality {

        @Test
        void shouldBeEqualWhenSameValue() {
            Phone phone1 = new Phone("11987654321");
            Phone phone2 = new Phone("11987654321");
            assertEquals(phone1, phone2);
            assertEquals(phone1.hashCode(), phone2.hashCode());
        }

        @Test
        void shouldNotBeEqualWhenDifferentValues() {
            Phone phone1 = new Phone("11987654321");
            Phone phone2 = new Phone("11911112222");
            assertNotEquals(phone1, phone2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            Phone phone = new Phone("11987654321");
            assertNotEquals(null, phone);
        }

        @Test
        void toStringShouldReturnValue() {
            Phone phone = new Phone("11987654321");
            assertEquals("11987654321", phone.toString());
        }
    }
}
