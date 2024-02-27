package org.morgan.bookstore.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.morgan.bookstore.enums.OrderStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OrderListing {

    private Integer id;
    private Integer totalItems;
    private Double amount;
    private String orderTrackingNumber;
    private LocalDateTime placedAt;
    private OrderStatus orderStatus;
}
