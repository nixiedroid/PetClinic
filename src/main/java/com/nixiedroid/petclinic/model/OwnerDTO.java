package com.nixiedroid.petclinic.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;
/**
 * Data Transfer Object (DTO) representing an owner.
 * <br>This record encapsulates owner data for use in communication between different layers of the application.
 */
@Builder
public record OwnerDTO(
        Long id,
        @NotNull(message = "Owner first name must not be null")
        @Size(min = 1, message = "First name must be at least 1 characters long")
        String firstName,
        @NotNull(message = "Owner last name must not be null")
        @Size(min = 1, message = "Last name must be at least 1 characters long")
        String lastName,
        String address,
        String city,
        String telephone,
        List<PetPlain> pets
) {}
