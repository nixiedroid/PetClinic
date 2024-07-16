package com.nixiedroid.petclinic.repository;

import com.nixiedroid.petclinic.model.Owner;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends ListCrudRepository<Owner, Long> {
    Optional<Owner> findDistinctById(Long id);
    boolean existsById(Long id);
}
