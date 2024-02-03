package org.morgan.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @NotNull(message = "Category name should not be null")
    @NotBlank(message = "Category name should not be blank")
    private String categoryName;

    @NotNull(message = "Section name should not be null")
    @NotBlank(message = "Section name should not be blank")
    private String sectionName;

    @Size(message = "Description should not exceed 500 characters", max = 500)
    private String description;

}
