package org.morgan.bookstore.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.Government;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressRequest {

    @NotBlank(message = "Name is required")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotNull(message = "Government is required")
    private Government government;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Full address is required")
    private String fullAddress;
}
