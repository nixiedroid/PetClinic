package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.model.Pet;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.service.ErrorMapper;
import com.nixiedroid.petclinic.service.PetDTOService;
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
    private final PetDTOService dtoService;
    private final ErrorMapper mapper;

    @Autowired
    public PetController(PetService petService, PetDTOService dtoService, ErrorMapper mapper) {
        this.petService = petService;
        this.dtoService = dtoService;
        this.mapper = mapper;
    }

    /**
     * Reject pets without owners
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(dtoService);
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
     * Listens for GET requests at <a href="/pets/{id}">/pets/{id}</a>
     *
     * @return json object {@link Pet} if {id} exists or null
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Long id) {
        Optional<PetDTO> pet = petService.getPetById(id);
        return pet.map(p -> new ResponseEntity<>(p,HttpStatus.OK))
                .orElseGet((() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }
    
    /**
     * Listens for POST requests at <a href="/pets">/pets</a>
     * and creates Pet object accordingly
     *
     * @return newly created json object {@link Pet} on success
     */
    @PostMapping
    public ResponseEntity<?> createPet(@Valid  @RequestBody PetDTO dto,Errors errors) {
        if(errors.hasErrors()) {
            return new ResponseEntity<>(mapper.apply(errors),HttpStatus.BAD_REQUEST);
        }
        if (dto.id() != null) {
            if (petService.existsById(dto.id())) {
                errors.rejectValue("id", "NULL", "Pet is already exists");
                return new ResponseEntity<>(mapper.apply(errors),HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(petService.savePet(dto), HttpStatus.CREATED);
    }

    /**
     * Listens for PUT requests at <a href="/pets/{id}">/pets/{id}</a>
     * and updates Pet object if {id} found
     * or creates Pet object if not-exists
     *
     * @return newly created json object {@link Pet} on success
     */
    @PutMapping("/{id}")
    ResponseEntity<?> putPet(@PathVariable Long id,@Valid @RequestBody PetDTO dto, Errors errors) {
        if(errors.hasErrors()) {
            return new ResponseEntity<>(mapper.apply(errors),HttpStatus.BAD_REQUEST);
        }
        if (dto.id() == null) {
            errors.rejectValue("id", "EXIST", "Id must not be null");
            return new ResponseEntity<>(mapper.apply(errors), HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(dto.id(), id)) {
            errors.rejectValue("id", "MISS", "Id mismatch");
            return new ResponseEntity<>(mapper.apply(errors), HttpStatus.BAD_REQUEST);
        }
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
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}