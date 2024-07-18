package com.nixiedroid.petclinic.model;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Plain representation of a pet.
 * <br>This record provides a simplified view of pet data without owner information
 */
public record PetPlain(
        Long id,
        @NotNull(message = "Pet name must not be null")
        @Size(min = 1, message = "Pet name must be at least 3 characters long")
        String name,
        LocalDate birthDate,
        String type
) {
        /**
         * Creates a list of {@link PetPlain} instances from a list of {@link Pet} entities.
         *
         * @param pets the list of {@link Pet} entities to create {@link PetPlain} instances from
         * @return the list of created {@link PetPlain} instances
         */
        public static List<PetPlain> create(@Nonnull List<Pet> pets) {
                return pets.stream().map(pet -> new PetPlain(
                        pet.getId(),
                        pet.getName(),
                        pet.getBirthDate(),
                        pet.getType()
                )).collect(Collectors.toList());
        }
}
