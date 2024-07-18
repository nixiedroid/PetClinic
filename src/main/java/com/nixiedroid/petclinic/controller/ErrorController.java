package com.nixiedroid.petclinic.controller;

import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global error handling controller for the application.
 * <br>This controller handles exceptions thrown by any controller and provides appropriate responses.
 * <br>
 * <br>Annotations used:
 * <ul>
 *   <li>{@link RestControllerAdvice}</li>
 * </ul>
 */
@RestControllerAdvice
public class ErrorController {

    /**
     * Handles any throwable exception that is not explicitly handled by other methods.
     * <br>Returns a response entity with HTTP status 500 (Internal Server Error) and a message containing
     * the exception class name and message.
     *
     * @param e the exception that was thrown
     * @return a ResponseEntity with status 500 and an error message
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> anyException(@Nonnull Exception e) {
        return ResponseEntity
                .internalServerError()
                .body("Internal Error: " + e.getClass().getName() + ": " + e.getMessage());
    }

    /**
     * Handles {@link NoResourceFoundException} exceptions.
     * <br>Returns a response entity with HTTP status 400 (Bad Request) and a generic bad request message.
     *
     * @return a ResponseEntity with status 400 and a bad request message
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> notFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link NoResourceFoundException} exceptions.
     * <br>Returns a response entity with HTTP status 400 (Bad Request) and a generic bad request message.
     *
     * @return a ResponseEntity with status 400 and a bad request message
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> methodNotAllowed() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }
}