package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.model.OwnerPlain;
import com.nixiedroid.petclinic.model.PetDTO;
import com.nixiedroid.petclinic.service.ErrorMapper;
import com.nixiedroid.petclinic.service.PetDTOService;
import com.nixiedroid.petclinic.service.PetService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @SuppressWarnings("unused")
    @MockBean
    private PetDTOService petDTOService;

    @SuppressWarnings("unused")
    @MockBean
    private ErrorMapper mapper;

    @SuppressWarnings("unused")
    @InjectMocks
    private PetController petController;



    @Test
    void getAllPets() throws Exception {
        Mockito.when(petService.getAllPets()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        PetDTO pet = createPetDTO();
        Mockito.when(petService.getAllPets()).thenReturn(Collections.singletonList(pet));
        mockMvc.perform(get("/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("A")));
    }

    @Test
    void getPetById() throws Exception {
        Long petId = 1L;
        Long nxID =-1L;
        PetDTO petDTO = createPetDTO();
        Mockito.when(petService.getPetById(petId)).thenReturn(Optional.of(petDTO));

        mockMvc.perform(get("/pets/{id}", petId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("A"));

        Mockito.when(petService.getPetById(nxID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pets/{id}", nxID))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPet() throws Exception {
        Long petId = 1L;

        Mockito.when(petDTOService.supports(PetDTO.class)).thenReturn(true);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"A\", \"type\": \"B\" }"))
                .andExpect(status().isCreated());

        Mockito.when(petService.existsById(petId)).thenReturn(true);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,  \"name\": \"A\", \"type\": \"B\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void putPet() throws Exception {
        Long petId = 1L;

        Mockito.when(petDTOService.supports(PetDTO.class)).thenReturn(true);

        mockMvc.perform(put("/pets/{id}",petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"A\", \"type\": \"B\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());

        mockMvc.perform(put("/pets/{id}", petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1}"))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/pets/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    void deletePet() throws Exception {
        Long petId = 1L;
        Long nxID =-1L;
        Mockito.when(petService.existsById(petId)).thenReturn(true);
        mockMvc.perform(delete("/pets/{id}", petId))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/pets/{id}", nxID))
                .andExpect(status().isBadRequest());
    }

    private PetDTO createPetDTO() {
        return PetDTO.builder()
                .id((long) 1).name("A").owner(OwnerPlain.builder().build()).build();
    }
}