package com.taskflow.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    /*
    METHOD: handleTaskNotFoundException
    DESCRIPTION: This method handles TaskNotFoundException which occurs when a requested task is not found
    PARAMETERS:
        - TaskNotFoundException ex: The exception object containing details about the not found task.
        - HttpServletRequest request: The HTTP request that resulted in the exception.
    RETURNS: ResponseEntity<ErrorResponse> - A response entity containing the error response and HTTP
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex, HttpServletRequest request)
    {
        // Creating error response for 404 Not Found
        ErrorResponse errorResp = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResp, HttpStatus.NOT_FOUND);
    }

    /*
    METHOD: handleValidationException
    DESCRIPTION: This method handles MethodArgumentNotValidException which occurs when validation on an argument annotated
    with @Valid fails. It constructs an ErrorResponse object containing details about the validation errors.
    PARAMETERS:
        - MethodArgumentNotValidException ex: The exception object containing validation error details.
        - HttpServletRequest request: The HTTP request that resulted in the exception.
    RETURNS: ResponseEntity<ErrorResponse> - A response entity containing the error response and HTTP
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request)
    {
        String message = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error-> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));  // Getting list of field errors from exception

        ErrorResponse errorResp = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message,
                request.getRequestURI()
                );

        return new ResponseEntity<>(errorResp, HttpStatus.BAD_REQUEST);
    }

    /*
    METHOD: handleGenericException
    DESCRIPTION: This method handles all other generic exceptions that are not specifically handled by other methods.
    PARAMETERS:
        - Exception ex: The exception object containing details about the error.
        - HttpServletRequest request: The HTTP request that resulted in the exception.
    RETURNS: ResponseEntity<ErrorResponse> - A response entity containing the error response and HTTP
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request)
    {
        ErrorResponse errorResp = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
