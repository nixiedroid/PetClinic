package com.nixiedroid.petclinic.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OwnerDTO(

        Long id,
        @NotNull
        @Size(min = 1, message = "First name must be at least 1 characters long")
        String firstName,
        @NotNull
        @Size(min = 1, message = "Last name must be at least 1 characters long")
        String lastName,

        String address,

        String city,

        String telephone,
        List<PetPlain> pets
) {


}
