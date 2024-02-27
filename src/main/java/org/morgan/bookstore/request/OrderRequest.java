package org.morgan.bookstore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.PaymentMethod;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderRequest {

    @NotNull
    @JsonProperty("shippingAddress")
    private AddressRequest shippingAddress;

    private PaymentMethod paymentMethod;
}
