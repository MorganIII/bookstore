package org.morgan.bookstore.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Provide your first name")
    private String firstName;

    @NotBlank(message = "Provide your last name")
    private String lastName;

    @Email(message = "Not valid email")
    private String email;

    @NotBlank(message = "You should provide a password")
    private String password;

}
