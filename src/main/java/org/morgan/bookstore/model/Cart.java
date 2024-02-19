package org.morgan.bookstore.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.morgan.bookstore.enums.CartStatus;
import org.morgan.bookstore.response.ItemDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true, exclude = {"cartItems"})
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer totalItems;

    private Double shippingPrice;

    private Double itemsPrice;

    private Double discountPrice;

    @Enumerated(EnumType.STRING)
    private CartStatus cartStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<CartItem> cartItems = new HashSet<>();



    public void clearCart() {
        this.setTotalItems(0);
        this.setDiscountPrice(0.0);
        this.setShippingPrice(0.0);
        this.setItemsPrice(0.0);
        this.setCoupon(null);
    }

}
