package com.nixiedroid.petclinic.repository;

import com.nixiedroid.petclinic.model.Owner;
import jakarta.annotation.Nonnull;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Owner} entities.
 * <br>This repository provides CRUD operations and additional methods to
 * interact with the {@link Owner} data in the database.
 * <br>
 * <br>Annotations used:
 * <ul>
 *   <li>{@link Repository}</li>
 * </ul>
 */
@Repository
public interface OwnerRepository extends ListCrudRepository<Owner, Long> {

    /**
     * Finds an {@link Owner} by its unique identifier.
     * <br>
     * <br>This method returns an Optional containing the distinct owner with the specified ID if it exists,
     * or an empty Optional if no owner is found.
     *
     * @param id the unique identifier of the owner
     * @return an Optional containing the found {@link Owner}, or empty if no owner is found
     */
    Optional<Owner> findDistinctById(Long id);

    /**
     * Checks if an {@link Owner} exists by its unique identifier.
     * <br>
     * <br>This method returns true if an owner with the specified ID exists, false otherwise.
     *
     * @param id the unique identifier of the owner
     * @return true if an owner with the specified ID exists, false otherwise
     */
    boolean existsById(@Nonnull Long id);
}