package com.nixiedroid.petclinic.service;
import com.nixiedroid.petclinic.model.Owner;
import com.nixiedroid.petclinic.model.OwnerDTO;
import com.nixiedroid.petclinic.model.Pet;
import com.nixiedroid.petclinic.model.PetPlain;
import com.nixiedroid.petclinic.repository.OwnerRepository;
import com.nixiedroid.petclinic.repository.PetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class OwnerServiceTest {
    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private OwnerService ownerService;


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
    public void testGetAllOwners() {
        Owner owner1 = Owner.builder()
                .firstName("A")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                        .pets(new ArrayList<>())
                        .build();

        Owner owner2 = Owner.builder()
                .firstName("D")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                        .pets(new ArrayList<>())
                        .build();
        List<Owner> owners = Arrays.asList(owner1, owner2);

        when(ownerRepository.findAll()).thenReturn(owners);

        List<OwnerDTO> result = ownerService.getAllOwners();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).firstName());
        assertEquals("D", result.get(1).firstName());
    }

    @Test
    public void testGetOwnerById_Exists() {
        Owner owner =  Owner.builder()
                .firstName("A")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                .pets(new ArrayList<>())
                .build();

        when(ownerRepository.findDistinctById(1L)).thenReturn(Optional.of(owner));

        Optional<OwnerDTO> result = ownerService.getOwnerById(1L);

        assertTrue(result.isPresent());
        assertEquals("A", result.get().firstName());
    }

    @Test
    public void testGetOwnerById_NotExists() {
        when(ownerRepository.findDistinctById(1L)).thenReturn(Optional.empty());

        Optional<OwnerDTO> result = ownerService.getOwnerById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testSaveOwner_CreateNew() {
        OwnerDTO ownerDTO = OwnerDTO.builder()
                .firstName("A")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                .pets(new ArrayList<>())
                .build();

        Owner ownerToSave = Owner.builder()
                .id(1L)
                .firstName("AAA")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                .pets(new ArrayList<>())
                .build();

        when(ownerRepository.save(any(Owner.class))).thenReturn(ownerToSave);

        OwnerDTO savedOwnerDTO = ownerService.saveOwner(ownerDTO);

        assertNotNull(savedOwnerDTO.id());
        assertEquals("AAA", savedOwnerDTO.firstName());
    }

    @Test
    public void testSaveOwner_UpdateExisting() {
        OwnerDTO ownerDTO = OwnerDTO.builder()
                .id(1L)
                .firstName("A")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                .pets(Collections.emptyList())
                .build();

        Owner existingOwner = Owner.builder()
                .id(1L)
                .firstName("AAA")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                .build();

        when(ownerRepository.findDistinctById(1L)).thenReturn(Optional.of(existingOwner));
        when(ownerRepository.save(any(Owner.class))).thenReturn(existingOwner);

        OwnerDTO updatedOwnerDTO = ownerService.saveOwner(ownerDTO);

        assertEquals(1L, updatedOwnerDTO.id());
        assertEquals("A", updatedOwnerDTO.firstName());
    }

    @Test
    public void testDeleteOwner_Exists() {
        doNothing().when(ownerRepository).deleteById(1L);

        assertDoesNotThrow(() -> ownerService.deleteOwner(1L));
    }

    @Test
    public void testDeleteOwner_NotExists() {
        doThrow(EmptyResultDataAccessException.class).when(ownerRepository).deleteById(1L);

        assertThrows(EmptyResultDataAccessException.class, () -> ownerService.deleteOwner(1L));
    }



    @Test
    public void testToDto() {
        Owner owner = Owner.builder()
                .id(1L)
                .firstName("AAA")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                .pets( Collections.emptyList())
                .build();

        when(petRepository.findDistinctById(anyLong())).thenReturn(Optional.empty());

        OwnerDTO ownerDTO = ownerService.toDto(owner);

        assertEquals(owner.getId(), ownerDTO.id());
        assertEquals(owner.getFirstName(), ownerDTO.firstName());
        assertEquals(owner.getLastName(), ownerDTO.lastName());
        assertEquals(owner.getAddress(), ownerDTO.address());
        assertEquals(owner.getCity(), ownerDTO.city());
        assertEquals(owner.getTelephone(), ownerDTO.telephone());
        assertEquals(owner.getPets().size(), ownerDTO.pets().size());
    }

    @Test
    public void testFillEntity() {
        Owner owner = new Owner();
        OwnerDTO ownerDTO = OwnerDTO.builder()
                .id(1L)
                .firstName("AAA")
                .lastName("B")
                .address("AA")
                .city("BB")
                .telephone("88005553535")
                .pets(Collections.singletonList(new PetPlain(1L, "A", null, "B")))
                .build();

        when(petRepository.findDistinctById(anyLong())).thenReturn(Optional.of(new Pet()));

        ownerService.fillEntity(owner, ownerDTO);

        assertEquals(ownerDTO.firstName(), owner.getFirstName());
        assertEquals(ownerDTO.lastName(), owner.getLastName());
        assertEquals(ownerDTO.address(), owner.getAddress());
        assertEquals(ownerDTO.city(), owner.getCity());
        assertEquals(ownerDTO.telephone(), owner.getTelephone());
        assertEquals(ownerDTO.pets().size(), owner.getPets().size());
    }

}
