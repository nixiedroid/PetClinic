package com.nixiedroid.petclinic.repository;

import com.nixiedroid.petclinic.model.Pet;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends ListCrudRepository<Pet, Long> {
    Optional<Pet> findDistinctById(Long id);
    boolean existsById(Long id);
}
