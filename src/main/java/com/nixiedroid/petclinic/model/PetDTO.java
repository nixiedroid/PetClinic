package com.nixiedroid.petclinic.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

public record PetDTO(
        Long id,
        @NotNull
        @Size(min = 3, message = "Pet name must be at least 3 characters long")
        String name,
        LocalDate birthDate,
        String type,
        OwnerPlain owner
) {


}
