package org.morgan.bookstore.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest{
    @Email(message = "Not valid email")
    private String email;

    @NotBlank(message = "You should provide a password")
    private String password;
}
