package org.morgan.bookstore.dto;

public record RegisterRequest(String firstName, String lastName, String email, String password) {
}
