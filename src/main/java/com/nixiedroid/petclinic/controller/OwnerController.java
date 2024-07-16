package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.model.Owner;
import com.nixiedroid.petclinic.model.OwnerDTO;
import com.nixiedroid.petclinic.service.ErrorMapper;
import com.nixiedroid.petclinic.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller class for  <a href="/Owners">/Owners</a> endpoint
 *
 * @see Owner
 * @see OwnerDTO
 */
@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;
    private final ErrorMapper mapper;

    @Autowired
    public OwnerController(OwnerService ownerService, ErrorMapper mapper) {
        this.ownerService = ownerService;
        this.mapper = mapper;
    }

    /**
     * Listens for GET requests at <a href="/Owners">/Owners</a>
     *
     * @return json list of {@link Owner}
     */
    @GetMapping
    public List<OwnerDTO> getAllOwners() {
        return ownerService.getAllOwners();
    }

    /**
     * Listens for GET requests at <a href="/Owners{id}">/Owners/{id}</a>
     *
     * @return json object {@link Owner} if {id} exists or null
     */
    @GetMapping("/{id}")
    public ResponseEntity<OwnerDTO> getOwnerById(@PathVariable Long id) {
        Optional<OwnerDTO> owner = ownerService.getOwnerById(id);
        return owner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Listens for POST requests at <a href="/Owners">/Owners</a>
     * and creates Owner object accordingly
     *
     * @return newly created json object {@link Owner} on success
     */
    @PostMapping
    public ResponseEntity<?> createOwner(@Valid @RequestBody OwnerDTO dto, Errors errors ) {
        if(errors.hasErrors()) return new ResponseEntity<>(mapper.apply(errors),HttpStatus.BAD_REQUEST);
        if (ownerService.existsById(dto.id())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(ownerService.saveOwner(dto), HttpStatus.CREATED);
    }

    /**
     * Listens for PUT requests at <a href="/Owners{id}">/Owners/{id}</a>
     * and updates Owner object if {id} found
     * or creates Owner object if not-exists
     *
     * @return newly created json object {@link Owner} on success
     */
    @PutMapping("/{id}")
    ResponseEntity<?> putOwner(@PathVariable Long id,@Valid @RequestBody OwnerDTO dto, Errors errors) {
        if(errors.hasErrors()) return new ResponseEntity<>(mapper.apply(errors),HttpStatus.BAD_REQUEST);
        if (!Objects.equals(dto.id(), id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (ownerService.existsById(id)) {
            return new ResponseEntity<>(ownerService.saveOwner(dto), HttpStatus.OK);
        } else return new ResponseEntity<>(ownerService.saveOwner(dto), HttpStatus.CREATED);
    }

    /**
     * Listens for DELETE requests at <a href="/Owners/{id}">/Owners/{id}</a>
     * and deletes Owner object if {id} found
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        if (ownerService.existsById(id)) {
            ownerService.deleteOwner(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}