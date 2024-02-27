package org.morgan.bookstore.order;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@RequiredArgsConstructor
public class OrderHandlerChain {

    private final AddressService addressService;
    private final CartService cartService;
    private final CouponService couponService;
    private final PaymentService paymentService;
    private final OrderService orderService;

    @Bean
    public OrderHandler orderHandler() {
        return OrderHandler.createChain(
                addressService,
                cartService,
                couponService,
                paymentService,
                orderService
        );
    }
}
