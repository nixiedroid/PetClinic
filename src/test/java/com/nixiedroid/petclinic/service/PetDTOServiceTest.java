package com.nixiedroid.petclinic.service;

import com.nixiedroid.petclinic.model.OwnerPlain;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SuppressWarnings("DataFlowIssue")
class PetDTOServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private PetDTOService petDTOService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations. openMocks(this);
    }

    @AfterEach
    public void shutDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testValidation_ValidPetDTO() {
        when(ownerRepository.existsById(1L)).thenReturn(true);
        PetDTO validPetDTO = PetDTO.builder()
                .id(1L)
                .name("B")
                .birthDate(null)
                .type("D")
                .owner(OwnerPlain.builder()
                        .id(1L)
                        .firstName("A")
                        .lastName("B")
                        .build())
                .build();
        Errors errors = new BeanPropertyBindingResult(validPetDTO, "validPetDTO");
        petDTOService.validate(validPetDTO, errors);
        assertEquals(0, errors.getErrorCount());
    }

    @Test
    public void testValidation_NullOwner() {
        PetDTO petDTO = PetDTO.builder()
                .id(1L)
                .name("B")
                .birthDate(null)
                .type("D")
                .owner(null)
                .build();
        Errors errors = new BeanPropertyBindingResult(petDTO, "petDTO");
        petDTOService.validate(petDTO, errors);
        assertEquals(1, errors.getErrorCount());
        assertEquals("Owner Must Not Be Null", errors.getFieldError("owner").getDefaultMessage());
    }

    @Test
    public void testValidation_NonExistentOwner() {
        when(ownerRepository.existsById(1L)).thenReturn(false);
        PetDTO petDTO = PetDTO.builder()
                .id(1L)
                .name("B")
                .birthDate(null)
                .type("D")
                .owner(OwnerPlain.builder()
                        .id(1L)
                        .firstName("A")
                        .lastName("B")
                        .build())
                .build();
        Errors errors = new BeanPropertyBindingResult(petDTO, "petDTO");
        petDTOService.validate(petDTO, errors);
        assertEquals(1, errors.getErrorCount());
        assertEquals("Not Existent Owner", errors.getFieldError("owner").getDefaultMessage());
    }

    @Test
    public void testValidation_NullPetName() {
        when(ownerRepository.existsById(1L)).thenReturn(true);
        PetDTO petDTO = PetDTO.builder()
                .id(1L)
                .name(null)
                .birthDate(null)
                .type("D")
                .owner(OwnerPlain.builder()
                        .id(1L)
                        .firstName("A")
                        .lastName("B")
                        .build())
                .build();
        Errors errors = new BeanPropertyBindingResult(petDTO, "petDTO");
        petDTOService.validate(petDTO, errors);
        assertEquals(1, errors.getErrorCount());
        assertEquals("Pet name must not be null", errors.getFieldError("name").getDefaultMessage());
    }

    @Test
    public void testValidation_EmptyPetName() {
        when(ownerRepository.existsById(1L)).thenReturn(true);
        PetDTO petDTO = PetDTO.builder()
                .id(1L)
                .name("")
                .birthDate(null)
                .type("D")
                .owner(OwnerPlain.builder()
                        .id(1L)
                        .firstName("A")
                        .lastName("B")
                        .build())
                .build();
        Errors errors = new BeanPropertyBindingResult(petDTO, "petDTO");
        petDTOService.validate(petDTO, errors);
        assertEquals(1, errors.getErrorCount());
        assertEquals("Pet name must be at least 1 characters long", errors.getFieldError("name").getDefaultMessage());
    }
}