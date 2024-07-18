package com.nixiedroid.petclinic.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.Errors;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ErrorMapperTest {
    @Mock
    private Errors errors;

    @InjectMocks
    private ErrorMapper errorMapper;

    @Test
    public void testErrorMapping() {
        when(errors.getFieldErrors()).thenReturn(List.of(
                createFieldError("fieldName1", "Error message 1"),
                createFieldError("fieldName2", "Error message 2")
        ));

        var errorMap = errorMapper.apply(errors);

        assertEquals(2, errorMap.size());
        assertEquals("Error message 1", errorMap.get("fieldName1"));
        assertEquals("Error message 2", errorMap.get("fieldName2"));
    }

    private org.springframework.validation.FieldError createFieldError(String field, String defaultMessage) {
        return new org.springframework.validation.FieldError(
                "objectName", field,
                null,
                false,
                null,
                null,
                defaultMessage);
    }
}