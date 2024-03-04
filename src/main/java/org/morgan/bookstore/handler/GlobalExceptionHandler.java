package org.morgan.bookstore.handler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.morgan.bookstore.exception.CouponException;
import org.morgan.bookstore.exception.ImageException;
import org.morgan.bookstore.exception.OrderException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exp, HttpServletRequest request) {
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
    public ResponseEntity<ErrorResponse> handleEntityExistsException(EntityExistsException exp, HttpServletRequest request) {
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
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp, HttpServletRequest request ) {
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
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exp, HttpServletRequest request ) {
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

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidImageExtensionException(ImageException exp, HttpServletRequest request ) {

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(CouponException.class)
    public ResponseEntity<ErrorResponse> handleCouponException(CouponException exp, HttpServletRequest request ) {

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException exp, HttpServletRequest request ) {

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(OrderException exp, HttpServletRequest request ) {

        ErrorResponse error = ErrorResponse.builder()
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getServletPath())
                .message(exp.getMessage())
                .build();
        log.error(exp.getMessage(), exp);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}
