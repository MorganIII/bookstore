package org.morgan.bookstore.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.CartStatus;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartResponse {


    private List<ItemDTO> items;

    private Integer totalItems;

    private Double cartSubTotal;

    private Double cartShippingPrice;

    private Double total;

    private String couponCode;

    private Double totalAfterDiscount;
}
