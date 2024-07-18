package com.nixiedroid.petclinic.service;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Component for mapping validation errors to a map of field error messages.
 * <br>This component implements the {@link Function} interface to convert {@link Errors} into a
 * map where the key is the field name and the value is the error message.
 * <br>
 */
@Component
public class ErrorMapper implements Function<Errors, Map<String, String>> {

    /**
     * Applies this function to the given {@link Errors} object.
     * <br>Extracts field errors and maps them to a {@link Map} where the key is the field name
     * and the value is the default error message.
     *
     * @param errors the Errors object containing validation errors
     * @return a map containing field names as keys and error messages as values
     */
    @Override
    public Map<String, String> apply(Errors errors) {
        Map<String, String> errorMap = new HashMap<>();
        errors.getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }
}