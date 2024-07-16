package com.nixiedroid.petclinic.repository;

import com.nixiedroid.petclinic.model.Owner;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends ListCrudRepository<Owner,Long> {
}
