package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.model.OwnerDTO;
import com.nixiedroid.petclinic.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Test
    public void getAllOwners_shouldReturnEmptyArray() throws Exception {
        Mockito.when(ownerService.getAllOwners()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void getAllOwners_shouldReturnOwners() throws Exception {
        OwnerDTO owner = new OwnerDTO(
                1L,
                "John", "Doe",
                null, null, null, null
        );

        Mockito.when(ownerService.getAllOwners()).thenReturn(Collections.singletonList(owner));

        mockMvc.perform(MockMvcRequestBuilders.get("/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[0].lastName", is("Doe")));
    }
}