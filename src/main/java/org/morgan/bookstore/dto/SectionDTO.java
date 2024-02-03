package org.morgan.bookstore.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SectionDTO {

    @NotNull(message = "Section name should not be null")
    @NotBlank(message = "Section name should not be blank")
    private String name;

    @Size(message = "Description should not exceed 500 characters", max = 500)
    private String description;
}
