package org.morgan.bookstore.model;

import jakarta.persistence.*;
import lombok.*;
import org.morgan.bookstore.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer totalItems;

    private Double totalPrice;

    private Double discount;

    private String orderTrackingNumber;

    private LocalDateTime placedAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    public Order(Integer id, Integer totalItems, String orderTrackingNumber, LocalDateTime placedAt, OrderStatus orderStatus, Payment payment) {
        this.id = id;
        this.totalItems = totalItems;
        this.orderTrackingNumber = orderTrackingNumber;
        this.placedAt = placedAt;
        this.orderStatus = orderStatus;
        this.payment = payment;
    }

    public Double getShippingPrice() {
        return shippingAddress.getGovernment().getShippingPrice();
    }

}
