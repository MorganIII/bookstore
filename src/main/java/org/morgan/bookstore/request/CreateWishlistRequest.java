package org.morgan.bookstore.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.Privacy;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateWishlistRequest {
    @NotBlank
    private String name;

    private String description;
    @NotNull
    private Privacy privacy;

    private Integer bookId;
}
