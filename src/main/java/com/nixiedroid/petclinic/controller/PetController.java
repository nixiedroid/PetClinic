package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.model.Pet;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.service.PetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller class for  <a href="/Pets">/Pets</a> endpoint
 *
 * @see Pet
 * @see PetDTO
 */
@RestController
@RequestMapping("/pets")
public class PetController {
   
    private final PetService petService;
    
    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    /**
     * Listens for GET requests at <a href="/Pets">/Pets</a>
     *
     * @return json list of {@link Pet}
     */
    @GetMapping
    public List<PetDTO> getAllPets() {
        return petService.getAllPets();
    }
    
    /**
     * Listens for GET requests at <a href="/Pets{id}">/Pets/{id}</a>
     *
     * @return json object {@link Pet} if {id} exists or null
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.getPetById(id);
        return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * Listens for POST requests at <a href="/Pets">/Pets</a>
     * and creates Pet object accordingly
     *
     * @return newly created json object {@link Pet} on success
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetDTO createPet(@Valid  @RequestBody PetDTO petDTO) {
        return petService.savePet(petDTO);
    }

    /**
     * Listens for PUT requests at <a href="/Pets{id}">/Pets/{id}</a>
     * and updates Pet object if {id} found
     * or creates Pet object if not-exists
     *
     * @return newly created json object {@link Pet} on success
     */
    @PutMapping("/{id}")
    ResponseEntity<PetDTO> putPet(@PathVariable Long id,@Valid @RequestBody PetDTO dto) {
        if (!Objects.equals(dto.id(), id)) new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if (petService.existsById(id)) {
            return new ResponseEntity<>(petService.savePet(dto), HttpStatus.OK);
        } else return new ResponseEntity<>(petService.savePet(dto), HttpStatus.CREATED);
    }

    /**
     * Listens for DELETE requests at <a href="/Pets/{id}">/Pets/{id}</a>
     * and deletes Pet object if {id} found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        if (petService.existsById(id)) {
            petService.deletePet(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}