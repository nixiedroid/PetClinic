package com.nixiedroid.petclinic.repository;

import com.nixiedroid.petclinic.model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OwnerRepositoryTest {

    @Mock
    private OwnerRepository ownerRepository;

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
        Owner owner = Owner.builder()
                .id(1L)
                .firstName("A")
                .lastName("B")
                .address("AAA")
                .city("BBB")
                .telephone("88005553535")
                .pets(null)
                .build();
        when(ownerRepository.findDistinctById(1L)).thenReturn(Optional.of(owner));
        when(ownerRepository.findDistinctById(2L)).thenReturn(Optional.empty());

        Optional<Owner> result = ownerRepository.findDistinctById(1L);
        Optional<Owner> notExist = ownerRepository.findDistinctById(2L);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().getFirstName());
        assertFalse(notExist.isPresent());
    }

    @Test
    void existsById() {
        when(ownerRepository.existsById(1L)).thenReturn(true);
        when(ownerRepository.existsById(2L)).thenReturn(false);

        boolean exists = ownerRepository.existsById(1L);
        boolean notExists = ownerRepository.existsById(2L);

        assertTrue(exists);
        assertFalse(notExists);
    }
}