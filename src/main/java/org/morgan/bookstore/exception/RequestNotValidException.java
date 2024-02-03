package org.morgan.bookstore.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class RequestNotValidException extends RuntimeException{

    private final Set<String> errorMessages;
}
