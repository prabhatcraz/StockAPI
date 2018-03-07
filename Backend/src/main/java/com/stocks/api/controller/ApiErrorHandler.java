package com.stocks.api.controller;

import com.stocks.api.exceptions.ResourceAlreadyExistsException;
import com.stocks.api.exceptions.ResourceNotFoundException;
import com.stocks.api.model.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

/**
 * Handles exceptions that may happen in the application and performs a nice wrapping over
 * the exception with a meaningful message.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(final ResourceNotFoundException ex) {
        final String message = Optional.of(ex.getMessage()).orElse("Resource not found.");
        final ErrorResponse errorResponse = new ErrorResponse("404", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExist(final ResourceAlreadyExistsException ex) {
        final String message = Optional.of(ex.getMessage()).orElse("Resource already exists.");
        final ErrorResponse errorResponse = new ErrorResponse("409", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericErrors(final Exception ex) {
        final String message = Optional.of(ex.getMessage()).orElse("Error occurred while serving the request.");
        final ErrorResponse errorResponse = new ErrorResponse("500", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
