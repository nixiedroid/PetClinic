package com.nixiedroid.petclinic.service;

import com.nixiedroid.petclinic.model.OwnerPlain;
import com.nixiedroid.petclinic.model.Pet;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.model.Owner;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import com.nixiedroid.petclinic.repository.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private PetService petService;

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
    public void testGetAllPets() {
        Pet pet1 = Pet.builder()
                .id(1L)
                .name("A")
                .birthDate(LocalDate.now())
                .type("B")
                .owner(new Owner())
                .build();

        Pet pet2 = Pet.builder()
                .id(2L)
                .name("D")
                .birthDate(LocalDate.now())
                .type("B")
                .owner(new Owner())
                .build();

        List<Pet> pets = Arrays.asList(pet1, pet2);
        when(petRepository.findAll()).thenReturn(pets);

        List<PetDTO> result = petService.getAllPets();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).name());
        assertEquals("D", result.get(1).name());
    }

    @Test
    public void testGetPetById_Exists() {
        Pet pet = Pet.builder()
                .id(1L)
                .name("A")
                .birthDate(LocalDate.now())
                .type("B")
                .owner(new Owner())
                .build();
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Optional<PetDTO> result = petService.getPetById(1L);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().name());
    }

    @Test
    public void testGetPetById_NotExists() {
        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<PetDTO> result = petService.getPetById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSavePet_CreateNew() {
        PetDTO petDTO = PetDTO.builder()
                .name("A")
                .birthDate(LocalDate.now())
                .type("B")
                .owner(OwnerPlain.builder()
                        .id(1L)
                        .firstName("A")
                        .lastName("B")
                        .build()
                )
                .build();

        Pet savedPet = Pet.builder()
                .id(1L)
                .name("A")
                .birthDate(LocalDate.now())
                .type("B")
                .owner(new Owner())
                .build();
        when(ownerRepository.findDistinctById(1L)).thenReturn(Optional.of(new Owner()));
        when(petRepository.save(any(Pet.class))).thenReturn(savedPet);

        PetDTO savedPetDTO = petService.savePet(petDTO);

        assertNotNull(savedPetDTO.id());
        assertEquals("A", savedPetDTO.name());
    }

    @Test
    public void testSavePet_UpdateExisting() {
        PetDTO petDTO = PetDTO.builder()
                .name("A")
                .birthDate(LocalDate.now())
                .type("B")
                .owner(OwnerPlain.builder()
                        .id(1L)
                        .firstName("A")
                        .lastName("B")
                        .build()
                )
                .build();

        Pet existingPet = Pet.builder()
                .id(1L)
                .name("AA")
                .birthDate(LocalDate.now())
                .type("BB")
                .owner(new Owner())
                .build();

        when(petRepository.findDistinctById(1L)).thenReturn(Optional.of(existingPet));
        when(ownerRepository.findDistinctById(1L)).thenReturn(Optional.of(new Owner()));
        when(petRepository.save(any(Pet.class))).thenReturn(existingPet);

        PetDTO updatedPetDTO = petService.savePet(petDTO);

        assertEquals(1L, updatedPetDTO.id());
        assertEquals("AA", updatedPetDTO.name());
    }

    @Test
    public void testDeletePet_Exists() {
        doNothing().when(petRepository).deleteById(1L);

        assertDoesNotThrow(() -> petService.deletePet(1L));
    }

    @Test
    public void testDeletePet_NotExists() {
        doThrow(EmptyResultDataAccessException.class).when(petRepository).deleteById(1L);

        assertThrows(EmptyResultDataAccessException.class, () -> petService.deletePet(1L));
    }
}
