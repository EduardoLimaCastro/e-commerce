package com.eduardocastro.user_service.domain.valueobject;

import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    private static Address valid() {
        return new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "01310100");
    }

    @Nested
    class Creation {

        @Test
        void shouldCreateWithValidData() {
            Address address = valid();

            assertEquals("Rua das Flores", address.getStreet());
            assertEquals("123", address.getNumber());
            assertNull(address.getComplement());
            assertEquals("Centro", address.getNeighborhood());
            assertEquals("São Paulo", address.getCity());
            assertEquals("SP", address.getState());
            assertEquals("01310100", address.getZipCode());
        }

        @Test
        void shouldStoreComplement() {
            Address address = new Address("Rua das Flores", "123", "Apto 4", "Centro", "São Paulo", "SP", "01310100");
            assertEquals("Apto 4", address.getComplement());
        }

        @Test
        void shouldTreatBlankComplementAsNull() {
            Address address = new Address("Rua das Flores", "123", "   ", "Centro", "São Paulo", "SP", "01310100");
            assertNull(address.getComplement());
        }

        @Test
        void shouldThrowWhenStreetIsBlank() {
            assertThrows(InvalidUserDataException.class,
                    () -> new Address("", "123", null, "Centro", "São Paulo", "SP", "01310100"));
        }

        @Test
        void shouldThrowWhenNumberIsNull() {
            assertThrows(InvalidUserDataException.class,
                    () -> new Address("Rua das Flores", null, null, "Centro", "São Paulo", "SP", "01310100"));
        }

        @Test
        void shouldThrowWhenStateIsNotTwoUppercaseLetters() {
            assertThrows(InvalidUserDataException.class,
                    () -> new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "sp", "01310100"));
        }

        @Test
        void shouldThrowWhenStateHasMoreThanTwoLetters() {
            assertThrows(InvalidUserDataException.class,
                    () -> new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SPP", "01310100"));
        }

        @Test
        void shouldThrowWhenZipCodeIsNotEightDigits() {
            assertThrows(InvalidUserDataException.class,
                    () -> new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "0131010"));
        }

        @Test
        void shouldThrowWhenZipCodeContainsLetters() {
            assertThrows(InvalidUserDataException.class,
                    () -> new Address("Rua das Flores", "123", null, "Centro", "São Paulo", "SP", "0131010A"));
        }
    }

    @Nested
    class Equality {

        @Test
        void shouldBeEqualWhenAllFieldsMatch() {
            Address a1 = valid();
            Address a2 = valid();

            assertEquals(a1, a2);
            assertEquals(a1.hashCode(), a2.hashCode());
        }

        @Test
        void shouldNotBeEqualWhenStreetDiffers() {
            Address a1 = valid();
            Address a2 = new Address("Av. Paulista", "123", null, "Centro", "São Paulo", "SP", "01310100");

            assertNotEquals(a1, a2);
        }

        @Test
        void shouldNotBeEqualWhenComplementDiffers() {
            Address a1 = new Address("Rua das Flores", "123", "Apto 1", "Centro", "São Paulo", "SP", "01310100");
            Address a2 = new Address("Rua das Flores", "123", "Apto 2", "Centro", "São Paulo", "SP", "01310100");

            assertNotEquals(a1, a2);
        }
    }
}
