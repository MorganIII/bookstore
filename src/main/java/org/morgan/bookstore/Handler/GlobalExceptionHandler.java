package org.morgan.bookstore.Handler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exp, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException(EntityExistsException exp, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp, HttpServletRequest request ) {
        Set<String> errorMessages = exp.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toSet());

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message("this request is not valid")
                .details(errorMessages)
                .build();

        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException exp, HttpServletRequest request ) {
        Set<String> errorMessages = exp.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message("this request is not valid")
                .details(errorMessages)
                .build();

        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
