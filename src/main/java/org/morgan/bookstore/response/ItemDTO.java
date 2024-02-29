package org.morgan.bookstore.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public ItemDTO(Integer itemId, Integer bookId, String title, Double actualPrice, Integer quantity, String bookThumbnail) {
        this.itemId = itemId;
        this.bookId = bookId;
        this.title = title;
        this.actualPrice = actualPrice;
        this.quantity = quantity;
        this.bookThumbnail = bookThumbnail;
    }
}
