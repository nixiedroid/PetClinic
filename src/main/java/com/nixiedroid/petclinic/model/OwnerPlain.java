package com.nixiedroid.petclinic.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OwnerPlain (

        Long id,
        @NotNull
        @Size(min=1, message="First name must be at least 1 characters long")
        String firstName,

        String lastName,

        String address,

        String city,

        String telephone

) {
}
