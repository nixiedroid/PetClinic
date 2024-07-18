package com.nixiedroid.petclinic.model;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a pet.
 * <br>This record encapsulates pet data for use in communication between different layers of the application.
 */
@Builder
public record PetDTO(
        Long id,
        String name,
        LocalDate birthDate,
        String type,
        OwnerPlain owner
) {
}
