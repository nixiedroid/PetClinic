package com.nixiedroid.petclinic.service;

import com.nixiedroid.petclinic.model.Owner;
import com.nixiedroid.petclinic.model.OwnerDTO;
import com.nixiedroid.petclinic.model.PetPlain;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import com.nixiedroid.petclinic.repository.PetRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing owners.
 * <br>This service provides methods to perform CRUD operations on {@link Owner} entities,
 * convert entities to DTOs, and handle related business logic.
 * <br>
 */
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;

    /**
     * Constructs a new {@link OwnerService} with the specified repositories.
     *
     * @param ownerRepository the repository for managing owners
     * @param petRepository the repository for managing pets
     */
    @Autowired
    public OwnerService(OwnerRepository ownerRepository, PetRepository petRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    /**
     * Retrieves all owners and converts them to DTOs.
     *
     * @return a list of {@link OwnerDTO} objects representing all owners
     */
    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream().map(this::toDto).toList();
    }

    /**
     * Retrieves an owner by their unique identifier and converts them to a DTO.
     *
     * @param id the unique identifier of the owner
     * @return an {@link Optional} containing the {@link OwnerDTO} if found, or empty if not found
     */
    public Optional<OwnerDTO> getOwnerById(@Nonnull Long id) {
        return ownerRepository.findDistinctById(id).map(this::toDto);
    }

    /**
     * Checks if an owner exists by their unique identifier.
     *
     * @param id the unique identifier of the owner
     * @return true if an owner with the specified ID exists, false otherwise
     */
    public boolean existsById(@Nonnull Long id) {
        return ownerRepository.existsById(id);
    }

    /**
     * Saves an owner based on the provided DTO.
     * <br>If the owner ID exists, it updates the existing owner; otherwise, it creates a new owner.
     *
     * @param dto the {@link OwnerDTO} containing the owner data
     * @return the saved {@link OwnerDTO}
     */
    public OwnerDTO saveOwner(@Nonnull OwnerDTO dto) {
        Optional<Owner> ownerEntity = Optional.empty();
        if (dto.id() != null) {
            ownerEntity = ownerRepository.findDistinctById(dto.id());
        }
        Owner o;
        if (ownerEntity.isEmpty()) { // Insert sequence
            o = new Owner();
            fillEntity(o, dto);
        } else { // Update sequence
            o = ownerEntity.get();
            fillEntity(o, dto);
        }
        return toDto(ownerRepository.save(o));
    }

    /**
     * Deletes an owner by their unique identifier.
     *
     * @param id the unique identifier of the owner
     */
    public void deleteOwner(@Nonnull Long id) {
        ownerRepository.deleteById(id);
    }

    /**
     * Converts an {@link Owner} entity to an {@link OwnerDTO}.
     *
     * @param o the {@link Owner} entity
     * @return the {@link OwnerDTO} representation of the owner
     */
    public OwnerDTO toDto(@Nonnull Owner o) {
        return OwnerDTO.builder()
                .id(o.getId())
                .firstName(o.getFirstName())
                .lastName(o.getLastName())
                .address(o.getAddress())
                .city(o.getCity())
                .telephone(o.getTelephone())
                .pets(PetPlain.create(o.getPets()))
                .build();
    }

    /**
     * Fills an {@link Owner} entity with data from an {@link OwnerDTO}.
     *
     * @param o the {@link Owner} entity to fill
     * @param dto the {@link OwnerDTO} containing the data
     */
    public void fillEntity(@Nonnull Owner o, @Nonnull OwnerDTO dto) {
        o.setFirstName(dto.firstName());
        o.setLastName(dto.lastName());
        o.setAddress(dto.address());
        o.setTelephone(dto.telephone());
        o.setCity(dto.city());
        o.setPets(dto.pets().stream()
                .map(p -> petRepository.findDistinctById(p.id()))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList()));
    }
}