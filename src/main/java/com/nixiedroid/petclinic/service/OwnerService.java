package com.nixiedroid.petclinic.service;

import com.nixiedroid.petclinic.model.Owner;
import com.nixiedroid.petclinic.model.OwnerDTO;
import com.nixiedroid.petclinic.model.OwnerPlain;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import com.nixiedroid.petclinic.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository, PetRepository petRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
    }

    public static OwnerPlain toPlain(Owner o) {
        return new OwnerPlain(
                o.getId(),
                o.getFirstName(),
                o.getLastName(),
                o.getAddress(),
                o.getCity(),
                o.getTelephone()
        );
    }

    public List<OwnerDTO> getAllOwners() {
        return ownerRepository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<OwnerDTO> getOwnerById(Long id) {
        return ownerRepository.findDistinctById(id).map(this::toDto);
    }

    public boolean existsById(Long id) {
        return ownerRepository.existsById(id);
    }

    public OwnerDTO saveOwner(OwnerDTO owner) {
        Owner o = ownerRepository.save(toEntity(owner));
        return toDto(o);
    }

    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }

    public OwnerDTO toDto(Owner o) {
        return new OwnerDTO(
                o.getId(),
                o.getFirstName(),
                o.getLastName(),
                o.getAddress(),
                o.getCity(),
                o.getTelephone(),
                PetService.toPlain(o.getPets()));
    }

    public Owner toEntity(OwnerDTO dto) { //SAVES OR UPDATES OWNER
        if (dto == null) throw new NullPointerException();
        Optional<Owner> ownerEntity = Optional.empty();
        if (dto.id() != null) {
            ownerEntity = ownerRepository.findDistinctById(dto.id());
        }
        Owner o;
        if (ownerEntity.isEmpty()) { //Insert sequence
            o = new Owner();
            o.setFirstName(dto.firstName());
            o.setLastName(dto.lastName());
            o.setAddress(dto.address());
            o.setTelephone(dto.telephone());
            o.setCity(dto.city());
        } else { //Update Sequence
            o = ownerEntity.get();
            o.setFirstName(dto.firstName());
            o.setLastName(dto.lastName());
            o.setAddress(dto.address());
            o.setTelephone(dto.telephone());
            o.setCity(dto.city());
            o.setPets(
                    dto.pets().stream()
                            .map(p -> petRepository.findDistinctById(p.id()))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList())
            );
        }
        return o;
    }
}
