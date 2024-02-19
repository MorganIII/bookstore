package org.morgan.bookstore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ItemDTO {

    private Integer itemId;

    private Integer bookId;

    private String title;

    @JsonProperty(value = "price")
    private Double actualPrice;

    private Integer quantity;

    private Double subTotal;

    private String bookThumbnail;
}
