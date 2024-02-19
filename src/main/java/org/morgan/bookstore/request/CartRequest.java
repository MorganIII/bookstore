package org.morgan.bookstore.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartRequest {

    @NotNull
    @Positive(message = "book id should be positive value")
    private Integer bookId;

    @Positive(message = "quantity should be positive value")
    private Integer quantity;
}
