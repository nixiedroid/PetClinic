package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.model.Pet;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.service.ErrorMapper;
import com.nixiedroid.petclinic.service.PetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller class for  <a href="/pets">/pets</a> endpoint
 *
 * @see Pet
 * @see PetDTO
 */
@RestController
@RequestMapping("/pets")
public class PetController {
   
    private final PetService petService;
    private final ErrorMapper mapper;

    @Autowired
    public PetController(PetService petService, ErrorMapper mapper) {
        this.petService = petService;
        this.mapper = mapper;
    }

    /**
     * Set Deep Pet Validator
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(petService);
    }

    /**
     * Listens for GET requests at <a href="/pets">/pets</a>
     *
     * @return json list of {@link Pet}
     */
    @GetMapping
    public List<PetDTO> getAllPets() {
        return petService.getAllPets();
    }
    
    /**
     * Listens for GET requests at <a href="/pets{id}">/pets/{id}</a>
     *
     * @return json object {@link Pet} if {id} exists or null
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Long id) {
        Optional<PetDTO> pet = petService.getPetById(id);
        return pet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * Listens for POST requests at <a href="/pets">/pets</a>
     * and creates Pet object accordingly
     *
     * @return newly created json object {@link Pet} on success
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPet(@Valid  @RequestBody PetDTO dto,Errors errors) {
        if(errors.hasErrors()) return new ResponseEntity<>(mapper.apply(errors),HttpStatus.BAD_REQUEST);
        if (petService.existsById(dto.id())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(petService.savePet(dto), HttpStatus.CREATED);
    }

    /**
     * Listens for PUT requests at <a href="/pets{id}">/pets/{id}</a>
     * and updates Pet object if {id} found
     * or creates Pet object if not-exists
     *
     * @return newly created json object {@link Pet} on success
     */
    @PutMapping("/{id}")
    ResponseEntity<?> putPet(@PathVariable Long id,@Valid @RequestBody PetDTO dto, Errors errors) {
        if(errors.hasErrors()) return new ResponseEntity<>(mapper.apply(errors),HttpStatus.BAD_REQUEST);
        if (!Objects.equals(dto.id(), id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (petService.existsById(id)) {
            return new ResponseEntity<>(petService.savePet(dto), HttpStatus.OK);
        } else return new ResponseEntity<>(petService.savePet(dto), HttpStatus.CREATED);
    }

    /**
     * Listens for DELETE requests at <a href="/pets/{id}">/pets/{id}</a>
     * and deletes Pet object if {id} found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        if (petService.existsById(id)) {
            petService.deletePet(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}