package com.nixiedroid.petclinic.service;

import com.nixiedroid.petclinic.model.Pet;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.model.PetPlain;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import com.nixiedroid.petclinic.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;

    @Autowired
    public PetService(OwnerRepository ownerRepository, PetRepository petRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    public List<PetDTO> getAllPets() {
        return petRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public PetDTO savePet(PetDTO pet) {
        Pet p = toEntity(pet);
        return toDto(petRepository.save(p));
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    public PetDTO toDto(Pet p) {
        return new PetDTO(
                p.getId(),
                p.getName(),
                p.getBirthDate(),
                p.getType(),
                OwnerService.toPlain(p.getOwner())
        );
    }

    public static List<PetPlain> toPlain(List<Pet> pets){
        return pets.stream().map(pet -> new PetPlain(
                        pet.getId(),
                        pet.getName(),
                        pet.getBirthDate(),
                        pet.getType()
                ))
                .collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return petRepository.existsById(id);
    }




    public Pet toEntity(PetDTO dto) { //SAVES OR UPDATES Pet
        if (dto == null) throw new NullPointerException();
        Optional<Pet> petEntity = Optional.empty();
        if (dto.id() != null) {
            petEntity = petRepository.findById(dto.id());
        }
        Pet o;
        if (petEntity.isEmpty()) { //Insert sequence
            o = new Pet();
            o.setName(dto.name());
            o.setType(dto.type());
            o.setBirthDate(dto.birthDate());
        } else { //Update Sequence
            o = petEntity.get();
            o.setName(dto.name());
            o.setType(dto.type());
            o.setBirthDate(dto.birthDate());
            o.setOwner(
                    ownerRepository.findDistinctById(dto.owner().id()).orElse(null)
            );
        }
        return o;
    }
}
