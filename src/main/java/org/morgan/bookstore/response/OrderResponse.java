package org.morgan.bookstore.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.OrderStatus;
import org.morgan.bookstore.enums.PaymentMethod;
import org.morgan.bookstore.enums.PaymentStatus;
import org.morgan.bookstore.request.AddressRequest;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderResponse {

    @JsonProperty("client_secret")
    private String clientSecret;
    private String orderTrackingNumber;
    private OrderStatus orderStatus;
    private LocalDateTime placedAt;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private List<ItemDTO> items;
    private Integer totalItems;
    private Double subTotal;
    private AddressRequest shippingAddress;
    private CouponResponse coupon;
    private Double total;
}
