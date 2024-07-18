package com.nixiedroid.petclinic.model;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Plain representation of an owner.
 * <br>This record provides a simplified view of owner data without pets
 */
@Builder
public record OwnerPlain(
        Long id,
        @NotNull(message = "Owner first name must not be null")
        @Size(min = 1, message = "First name must be at least 1 characters long")
        String firstName,
        @NotNull(message = "Owner last name must not be null")
        @Size(min = 1, message = "Last name must be at least 1 characters long")
        String lastName,
        String address,
        String city,
        String telephone
) {
    /**
     * Creates an {@link OwnerPlain} instance from an {@link Owner} entity.
     *
     * @param o the {@link Owner} entity to create the {@link OwnerPlain} from
     * @return the created {@link OwnerPlain} instance
     */
    public static OwnerPlain create(@Nonnull Owner o) {
        return OwnerPlain.builder()
                .id(o.getId())
                .firstName(o.getFirstName())
                .lastName(o.getLastName())
                .address(o.getAddress())
                .city(o.getCity())
                .telephone(o.getTelephone())
                .build();
    }
}
