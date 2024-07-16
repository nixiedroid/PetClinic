package com.nixiedroid.petclinic.service;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class ErrorMapper implements Function<Errors,Map<String, String>> {
    @Override
    public Map<String, String> apply(Errors errors) {
        Map<String, String> errorMap = new HashMap<>();
        errors.getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }
}
