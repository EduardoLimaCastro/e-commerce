package com.eduardocastro.user_service.domain.valueobject;

import com.eduardocastro.user_service.domain.exception.InvalidUserDataException;

import java.util.Objects;

public final class Address {

    private final String street;
    private final String number;
    private final String complement;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String zipCode;

    public Address(String street, String number, String complement, String neighborhood, String city, String state, String zipCode) {
        if (street == null || street.isBlank())
            throw new InvalidUserDataException("Street cannot be null or blank");
        if (number == null || number.isBlank())
            throw new InvalidUserDataException("Number cannot be null or blank");
        if (neighborhood == null || neighborhood.isBlank())
            throw new InvalidUserDataException("Neighborhood cannot be null or blank");
        if (city == null || city.isBlank())
            throw new InvalidUserDataException("City cannot be null or blank");
        if (state == null || !state.matches("^[A-Z]{2}$"))
            throw new InvalidUserDataException("State must be 2 uppercase letters (e.g. SP): " + state);
        if (zipCode == null || !zipCode.matches("^\\d{8}$"))
            throw new InvalidUserDataException("Zip code must be 8 digits (e.g. 01310100): " + zipCode);

        this.street = street;
        this.number = number;
        this.complement = (complement == null || complement.isBlank()) ? null : complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address other)) return false;
        return Objects.equals(street, other.street)
                && Objects.equals(number, other.number)
                && Objects.equals(complement, other.complement)
                && Objects.equals(neighborhood, other.neighborhood)
                && Objects.equals(city, other.city)
                && Objects.equals(state, other.state)
                && Objects.equals(zipCode, other.zipCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, complement, neighborhood, city, state, zipCode);
    }
}
