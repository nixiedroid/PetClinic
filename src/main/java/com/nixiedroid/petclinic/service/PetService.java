package com.nixiedroid.petclinic.service;

import com.nixiedroid.petclinic.model.OwnerPlain;
import com.nixiedroid.petclinic.model.Pet;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import com.nixiedroid.petclinic.repository.PetRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing pets.
 * <br>This service provides methods to perform CRUD operations on {@link Pet} entities,
 * convert entities to DTOs, and handle related business logic.
 * <br>
 */
@Service
public class PetService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;

    /**
     * Constructs a new {@link PetService} with the specified repositories.
     *
     * @param ownerRepository the repository for managing owners
     * @param petRepository the repository for managing pets
     */
    @Autowired
    public PetService(OwnerRepository ownerRepository, PetRepository petRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    /**
     * Retrieves all pets and converts them to DTOs.
     *
     * @return a list of {@link PetDTO} objects representing all pets
     */
    public List<PetDTO> getAllPets() {
        return petRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a pet by its unique identifier and converts it to a DTO.
     *
     * @param id the unique identifier of the pet
     * @return an {@link Optional} containing the {@link PetDTO} if found, or empty if not found
     */
    public Optional<PetDTO> getPetById(@Nonnull Long id) {
        return petRepository.findById(id).map(this::toDto);
    }

    /**
     * Saves a pet based on the provided DTO.
     * <br>If the pet ID exists, it updates the existing pet; otherwise, it creates a new pet.
     *
     * @param dto the {@link PetDTO} containing the pet data
     * @return the saved {@link PetDTO}
     */
    public PetDTO savePet(@Nonnull PetDTO dto) {
        Optional<Pet> petEntity = Optional.empty();
        if (dto.id() != null) {
            petEntity = petRepository.findById(dto.id());
        }
        Pet p;
        if (petEntity.isEmpty()) { // Insert sequence
            p = new Pet();
            fillEntity(p, dto);
        } else { // Update sequence
            p = petEntity.get();
            fillEntity(p, dto);
        }
        return toDto(petRepository.save(p));
    }

    /**
     * Deletes a pet by its unique identifier.
     *
     * @param id the unique identifier of the pet
     */
    public void deletePet(@Nonnull Long id) {
        petRepository.deleteById(id);
    }

    /**
     * Converts a {@link Pet} entity to a {@link PetDTO}.
     *
     * @param p the {@link Pet} entity
     * @return the {@link PetDTO} representation of the pet
     */
    public PetDTO toDto(@Nonnull Pet p) {
        return PetDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .birthDate(p.getBirthDate())
                .type(p.getType())
                .owner(OwnerPlain.create(p.getOwner()))
                .build();
    }

    /**
     * Checks if a pet exists by its unique identifier.
     *
     * @param id the unique identifier of the pet
     * @return true if a pet with the specified ID exists, false otherwise
     */
    public boolean existsById(@Nonnull Long id) {
        return petRepository.existsById(id);
    }

    /**
     * Fills a {@link Pet} entity with data from a {@link PetDTO}.
     *
     * @param p the {@link Pet} entity to fill
     * @param dto the {@link PetDTO} containing the data
     */
    public void fillEntity(@Nonnull Pet p, @Nonnull PetDTO dto) {
        p.setName(dto.name());
        p.setType(dto.type());
        p.setBirthDate(dto.birthDate());
        p.setOwner(ownerRepository.findDistinctById(dto.owner().id())
                .orElseThrow(IllegalArgumentException::new));
    }
}
