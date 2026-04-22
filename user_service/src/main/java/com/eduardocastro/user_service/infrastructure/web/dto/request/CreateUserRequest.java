package com.eduardocastro.user_service.infrastructure.web.dto.request;

import com.eduardocastro.user_service.domain.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Pattern(regexp = "^\\d{11}$", message = "Phone must be 11 digits") String phone,
        @NotBlank @Email String email,
        @NotNull @Valid AddressRequest address,
        @NotNull UserRole role
) {
    public record AddressRequest(
            @NotBlank String street,
            @NotBlank String number,
            String complement,
            @NotBlank String neighborhood,
            @NotBlank String city,
            @NotBlank @Size(min = 2, max = 2) String state,
            @NotBlank @Pattern(regexp = "^\\d{8}$", message = "Zip code must be 8 digits") String zipCode
    ) {}
}
