package com.nixiedroid.petclinic.repository;

import com.nixiedroid.petclinic.model.Pet;
import jakarta.annotation.Nonnull;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Pet} entities.
 * <br>This repository provides CRUD operations and additional methods to
 * interact with the {@link Pet} data in the database.
 * <br>
 * <br>Annotations used:
 * <ul>
 *   <li>{@link Repository}</li>
 * </ul>
 */
@Repository
public interface PetRepository extends ListCrudRepository<Pet, Long> {

    /**
     * Finds a {@link Pet} by its unique identifier.
     * <br>This method returns an Optional containing the distinct pet with the specified ID if it exists,
     * or an empty Optional if no pet is found.
     *
     * @param id the unique identifier of the pet
     * @return an Optional containing the found {@link Pet}, or empty if no pet is found
     */
    Optional<Pet> findDistinctById(Long id);

    /**
     * Checks if a {@link Pet} exists by its unique identifier.
     * <br>This method returns true if a pet with the specified ID exists, false otherwise.
     *
     * @param id the unique identifier of the pet
     * @return true if a pet with the specified ID exists, false otherwise
     */
    boolean existsById(@Nonnull Long id);
}