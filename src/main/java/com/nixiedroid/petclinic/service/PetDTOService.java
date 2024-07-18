package com.nixiedroid.petclinic.service;

import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Service class for validating {@link PetDTO} objects.
 * <br>This service implements the {@link Validator} interface to provide validation logic for pet data transfer objects (DTOs).
 * <br>
 */
@Service
public class PetDTOService implements Validator {

    private final OwnerRepository ownerRepository;

    /**
     * Constructs a new {@link PetDTOService} with the specified repository.
     *
     * @param ownerRepository the repository for managing owners
     */
    @Autowired
    public PetDTOService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Determines if the validator supports the given class.
     * <br>This implementation checks if the provided class is {@link PetDTO}.
     *
     * @param clazz the class to check
     * @return true if the class is {@link PetDTO}, false otherwise
     */
    @Override
    public boolean supports(@Nonnull Class<?> clazz) {
        return PetDTO.class.equals(clazz);
    }

    /**
     * Validates the given target object.
     * <br>This method checks if the {@link PetDTO} has a non-null owner and if the owner exists in the repository.
     * <br>If validation fails, appropriate error messages are added to the {@link Errors} object.
     *
     * @param target the object to validate
     * @param errors the errors object to add validation errors to
     */
    @Override
    public void validate(@Nonnull Object target, @Nonnull Errors errors) {
        PetDTO pet = (PetDTO) target;
        if (pet.owner() == null) {
            errors.rejectValue("owner", "owner", "Owner Must Not Be Null");
        } else if (!ownerRepository.existsById(pet.owner().id())) {
            errors.rejectValue("owner", "owner", "Not Existent Owner");
        }
        if (pet.name() == null) {
            errors.rejectValue("name", "owner",  "Pet name must not be null");
        } else if (pet.name().isEmpty()) {
            errors.rejectValue("name", "owner",  "Pet name must be at least 1 characters long");
        }
    }
}