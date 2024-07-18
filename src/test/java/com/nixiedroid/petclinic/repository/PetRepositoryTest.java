package com.nixiedroid.petclinic.repository;

import com.nixiedroid.petclinic.model.Pet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PetRepositoryTest {
    @Mock
    private PetRepository petRepository;

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
    void findDistinctById() {

        Pet pet = Pet.builder()
                .id(1L)
                .name("A")
                .type("B")
                .owner(null)
                .birthDate(LocalDate.now())
                .build();

        when(petRepository.findDistinctById(1L)).thenReturn(Optional.of(pet));

        Optional<Pet> result = petRepository.findDistinctById(1L);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getName());
    }

    @Test
    void existsById() {
        when(petRepository.existsById(1L)).thenReturn(true);
        when(petRepository.existsById(2L)).thenReturn(false);

        boolean exists = petRepository.existsById(1L);
        boolean notExists = petRepository.existsById(2L);

        assertTrue(exists);
        assertFalse(notExists);
    }
}