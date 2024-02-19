package org.morgan.bookstore.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartPriceResponse {

    private Double oldPrice;

    private Double newPrice;
}
