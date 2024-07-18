package com.nixiedroid.petclinic.controller;

import com.nixiedroid.petclinic.service.ErrorMapper;
import com.nixiedroid.petclinic.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = {ErrorController.class,OwnerController.class})
class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @SuppressWarnings("unused")
    @MockBean
    private ErrorMapper mapper;

    @Test
    void anyException() throws Exception {
        Mockito.when(ownerService.getAllOwners()).thenThrow(new RuntimeException("Test Exception"));
        mockMvc.perform(MockMvcRequestBuilders.get("/owners"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content()
                        .string("Internal Error: java.lang.RuntimeException: Test Exception"));
    }

    @Test
    void notFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/test-not-found"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}