package com.nixiedroid.petclinic.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PetPlainTest {

    @Test
    public void testCreatePetPlainList() {
        Pet pet1 = Pet.builder()
                .id(1L)
                .name("A")
                .birthDate(LocalDate.now())
                .type("B")
                .owner(null)
                .build();

        Pet pet2 = Pet.builder()
                .id(2L)
                .name("B")
                .birthDate(LocalDate.now())
                .type("F")
                .owner(null)
                .build();
        List<Pet> pets = List.of(pet1, pet2);

        List<PetPlain> petPlains = PetPlain.create(pets);

        assertEquals(2, petPlains.size());

        PetPlain plain1 = petPlains.get(0);
        assertEquals(pet1.getId(), plain1.id());
        assertEquals(pet1.getName(), plain1.name());
        assertEquals(pet1.getBirthDate(), plain1.birthDate());
        assertEquals(pet1.getType(), plain1.type());

        PetPlain plain2 = petPlains.get(1);
        assertEquals(pet2.getId(), plain2.id());
        assertEquals(pet2.getName(), plain2.name());
        assertEquals(pet2.getBirthDate(), plain2.birthDate());
        assertEquals(pet2.getType(), plain2.type());
    }
}