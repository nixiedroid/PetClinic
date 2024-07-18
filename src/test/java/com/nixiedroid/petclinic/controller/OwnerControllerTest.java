package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.model.OwnerDTO;
import com.nixiedroid.petclinic.service.ErrorMapper;
import com.nixiedroid.petclinic.service.OwnerService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @SuppressWarnings("unused")
    @MockBean
    private ErrorMapper mapper;

    @SuppressWarnings("unused")
    @InjectMocks
    private OwnerController ownerController;

    @Test
    public void testGetAllOwners() throws Exception {
        Mockito.when(ownerService.getAllOwners()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        OwnerDTO owner = createOwnerDTO();
        Mockito.when(ownerService.getAllOwners()).thenReturn(Collections.singletonList(owner));
        mockMvc.perform(get("/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("A")))
                .andExpect(jsonPath("$[0].lastName", is("B")));
    }

    @Test
    public void testGetOwnerByIdFound() throws Exception {
        Long ownerId = 1L;
        Long nxID =-1L;
        OwnerDTO ownerDTO = createOwnerDTO();
        Mockito.when(ownerService.getOwnerById(ownerId)).thenReturn(Optional.of(ownerDTO));

        mockMvc.perform(get("/owners/{id}", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("A"))
                .andExpect(jsonPath("$.lastName").value("B"));

        Mockito.when(ownerService.getOwnerById(nxID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/owners/{id}", nxID))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOwner() throws Exception {
        Long ownerId = 1L;
        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstName\": \"A\", \"lastName\": \"B\" }"))
                .andExpect(status().isCreated());

        Mockito.when(ownerService.existsById(ownerId)).thenReturn(true);

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,  \"firstName\": \"A\", \"lastName\": \"B\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());

        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"\", \"lastName\": \"B\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());

    }

    @Test
    public void testPutOwner() throws Exception {
        Long ownerId = 1L;

        mockMvc.perform(put("/owners/{id}",ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1,\"firstName\": \"\", \"lastName\": \"B\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());

        mockMvc.perform(put("/owners/{id}",ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"A\", \"lastName\": \"B\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());

        mockMvc.perform(put("/owners/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"firstName\": \"A\", \"lastName\": \"B\" }"))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/owners/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"\": \"A\", \"lastName\": \"B\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isMap());
    }

    @Test
    public void testDeleteOwner() throws Exception {
        Long ownerId = 1L;
        Long nxID =-1L;
        Mockito.when(ownerService.existsById(ownerId)).thenReturn(true);
        mockMvc.perform(delete("/owners/{id}", ownerId))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/owners/{id}", nxID))
                .andExpect(status().isBadRequest());
    }

    private OwnerDTO createOwnerDTO() {
        return OwnerDTO.builder()
                .id((long) 1).firstName("A").lastName("B")
                .address(null).city(null).telephone(null).pets(null).build();
    }


}